package by.vitstep.organizer.service.analytics;

import by.vitstep.organizer.config.ProjectConfiguration;
import by.vitstep.organizer.exception.AccountNotFoundException;
import by.vitstep.organizer.model.dto.ananlytics.*;
import by.vitstep.organizer.model.dto.enums.ArchiveStatsType;
import by.vitstep.organizer.model.entity.Account;
import by.vitstep.organizer.model.entity.Transaction;
import by.vitstep.organizer.model.entity.User;
import by.vitstep.organizer.repository.AccountRepository;
import by.vitstep.organizer.repository.FriendRepository;
import by.vitstep.organizer.repository.TransactionRepository;
import by.vitstep.organizer.utils.SecurityUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AnalyticsService {
    TransactionRepository transactionRepository;
    EntityManager entityManager;
    AccountRepository accountRepository;
    ProjectConfiguration projectConfiguration;
    FriendRepository friendRepository;

    @Transactional
    public List<AbstractAnalyticsResponseDto> getTxAnalytics(AnalyticsRequestDto requestDto) throws AuthenticationException {
        String query = buildingQuery(requestDto);
        Query entityQuery = entityManager.createNativeQuery(query, Transaction.class);
        List<Transaction> transactions = entityQuery.getResultList();

        if (ObjectUtils.isEmpty(requestDto.getFriendsIdList())) {
            return List.of(buildingAnalyticsResponseDto(requestDto, transactions, null));
        }
        /*Map<Long, List<Transaction>> friendTxMap = transactions
                .stream()
                .collect(Collectors.toMap(
                        tx -> tx.getFriend().getId(),
                        tx1 -> new ArrayList<Transaction>(Collections.singleton(tx1)),
                        (list1, list2) -> {
                            list1.addAll(list2);
                            return list1;
                        }
                ));*/

        Map<Long, List<Transaction>> friendTxMap = new HashMap<>();
        for (Transaction transaction : transactions) {
            Long friendId = transaction.getFriend().getId();
            if (friendTxMap.containsKey(friendId)) {
                List<Transaction> tmp = friendTxMap.get(friendId);
                tmp.add(transaction);
                friendTxMap.replace(friendId, tmp);
            } else {
                List<Transaction> tmp = new ArrayList<>();
                tmp.add(transaction);
                friendTxMap.put(friendId, tmp);
            }
        }
        List<AbstractAnalyticsResponseDto> resultList = new ArrayList<>();
        for (Map.Entry<Long, List<Transaction>> entry : friendTxMap.entrySet()) {
            resultList.add(buildingAnalyticsResponseDto(requestDto, entry.getValue(), entry.getKey()));

        }
        return resultList;
    }

    private String buildingQuery(AnalyticsRequestDto requestDto) {
        String query = "select * from transaction where ";

        if (requestDto.getType() == ArchiveStatsType.ALL) {
            query = query.concat("(target_account= ")
                    .concat(requestDto.getAccountId().toString())
                    .concat(" or ")
                    .concat("source_account= ")
                    .concat(requestDto.getAccountId().toString())
                    .concat(") ");
        }
        if (requestDto.getType() == ArchiveStatsType.INCOME) {
            query = query.concat("target_account= ")
                    .concat(requestDto.getAccountId().toString()
                            .concat(" "));
        }
        if (requestDto.getType() == ArchiveStatsType.SPEND) {
            query = query.concat("source_account= ")
                    .concat(requestDto.getAccountId().toString()
                            .concat(" "));
        }
        if (ObjectUtils.isNotEmpty(requestDto.getDateFrom())) {
            query = query.concat("and date_time> ")
                    .concat(requestDto.getDateFrom().toString()
                            .concat(" "));
        }
        if (ObjectUtils.isNotEmpty(requestDto.getDateTo())) {
            query = query.concat("and date_time< ")
                    .concat(requestDto.getDateTo().toString()
                            .concat(" "));
        }
        if (ObjectUtils.isNotEmpty(requestDto.getLessThan())) {
            query = query.concat("and amount< ")
                    .concat(requestDto.getLessThan().toString()
                            .concat(" "));
        }
        if (ObjectUtils.isNotEmpty(requestDto.getGreaterThan())) {
            query = query.concat("and amount> ")
                    .concat(requestDto.getGreaterThan().toString()
                            .concat(" "));
        }
        if (ObjectUtils.isNotEmpty(requestDto.getFriendsIdList())) {
            query = query.concat("friend_id= ")
                    .concat(requestDto.getFriendsIdList().toString()
                            .concat(" "));
        }
        return query.concat(";");

    }

    private AbstractAnalyticsResponseDto buildingAnalyticsResponseDto(AnalyticsRequestDto requestDto, List<Transaction> transactions, Long friendId) throws AuthenticationException {
        User user = SecurityUtil.getCurrentUser().orElseThrow(AuthenticationException::new);
        Account account = accountRepository.findByIdAndUser(requestDto.getAccountId(), user)
                .orElseThrow(() -> new AccountNotFoundException(requestDto.getAccountId()));

        if (ObjectUtils.isEmpty(transactions)) {
            return getEmptyAnalyticsResponseDto(requestDto, account, friendId);
        }
        if (requestDto.getType() != ArchiveStatsType.ALL) {
            return getSingleTypeResponseDto(requestDto, account, transactions);
        }
        return getAllTypesResponseDto(requestDto, account, transactions);
    }

    private MultipleTypesAnalyticsResponseDto getAllTypesResponseDto(AnalyticsRequestDto requestDto, Account account, List<Transaction> transactions) {
        MultipleTypesAnalyticsResponseDto.MultipleTypesAnalyticsResponseDtoBuilder builder = MultipleTypesAnalyticsResponseDto.builder()
                .incomeAmount(transactions.stream()
                        .filter(tx -> tx.getTargetAccount().getId().equals(account.getId()))
                        .map(Transaction::getAmount)
                        .reduce(Float::sum)
                        .orElse(0f))
                .spendAmount(transactions.stream()
                        .filter(tx -> tx.getSourceAccount().getId().equals(account.getId()))
                        .map(Transaction::getAmount)
                        .reduce(Float::sum)
                        .orElse(0F))
                .accountName(account.getName())
                .friend(buildFrendInfo(transactions));
        buildDate(requestDto, builder);
        return builder.build();
    }

    private SingleTypeAnalyticsResponseDto getSingleTypeResponseDto(AnalyticsRequestDto requestDto, Account account, List<Transaction> transactions) {
        SingleTypeAnalyticsResponseDto.SingleTypeAnalyticsResponseDtoBuilder builder = SingleTypeAnalyticsResponseDto.builder()
                .accountName(account.getName())
                .amount(transactions.stream()
                        .map(Transaction::getAmount)
                        .reduce(Float::sum)
                        .orElse(0F))
                .friend(buildFrendInfo(transactions));
        buildDate(requestDto, builder);
        return builder.build();
    }

    private EmptyAnalyticsResponseDto getEmptyAnalyticsResponseDto(AnalyticsRequestDto requestDto, Account account, Long friendId) {
        EmptyAnalyticsResponseDto.EmptyAnalyticsResponseDtoBuilder builder = EmptyAnalyticsResponseDto.builder();
        builder.accountName(account.getName());
        builder.message("Данные не найдены");
        if (ObjectUtils.isNotEmpty(friendId)) {
            builder.friend(friendRepository.findById(friendId)
                    .map(friend -> FriendShortInfoDto.builder()
                            .id(friendId)
                            .name(friend.getName())
                            .build())
                    .orElse(null));
        }
        buildDate(requestDto, builder);
        return builder.build();
    }

    private FriendShortInfoDto buildFrendInfo(List<Transaction> transactions) {
        return transactions
                .stream()
                .filter(tx -> Objects.nonNull(tx.getFriend()))
                .findFirst()
                .map(Transaction::getFriend)
                .map(friend -> FriendShortInfoDto.builder()
                        .id(friend.getId())
                        .name(friend.getName())
                        .build())
                .orElse(null);
    }

    //Метод для определения периода вывода аналитики
    private <T extends AbstractAnalyticsResponseDto.AbstractAnalyticsResponseDtoBuilder> void buildDate
    (AnalyticsRequestDto requestDto, T builder) {
        builder.dateFrom(ObjectUtils.isNotEmpty(requestDto.getDateFrom()) ?
                requestDto.getDateFrom() :
                LocalDateTime.now().minusDays(projectConfiguration.getBusiness().getArchivationPeriodDays()));

        builder.dateTo(ObjectUtils.isNotEmpty(requestDto.getDateTo()) ?
                requestDto.getDateTo() :
                LocalDateTime.now());
    }
}
