package by.vitstep.organizer.service.analytics;

import by.vitstep.organizer.config.ProjectConfiguration;
import by.vitstep.organizer.exception.AccountNotFoundException;
import by.vitstep.organizer.model.dto.ananlytics.*;
import by.vitstep.organizer.model.dto.enums.ArchiveStatsType;
import by.vitstep.organizer.model.entity.Account;
import by.vitstep.organizer.model.entity.Transaction;
import by.vitstep.organizer.model.entity.User;
import by.vitstep.organizer.repository.AccountRepository;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AnalyticsService {
    TransactionRepository transactionRepository;
    EntityManager entityManager;
    AccountRepository accountRepository;
    ProjectConfiguration projectConfiguration;

    @Transactional
    public List<AbstractAnalyticsResponseDto> getTxAnalytics(AnalyticsRequestDto requestDto) throws AuthenticationException {
        String query = buildingQuery(requestDto);
        Query entityQuery = entityManager.createNativeQuery(query, Transaction.class);
        List<Transaction> transactions = entityQuery.getResultList();

        Map<Long, List<Transaction>> friendTxMap = new HashMap<>();
        List<AbstractAnalyticsResponseDto> resultList = new ArrayList<>();
        List<Transaction> tmp = new ArrayList<>();
        for (Transaction transaction : transactions) {
            Long friendId = transaction.getFriend().getId();

            if (friendTxMap.containsKey(friendId)) {
                tmp = friendTxMap.get(friendId);
                tmp.add(transaction);
                friendTxMap.replace(friendId, tmp);
            } else {
                tmp.add(transaction);
                friendTxMap.put(friendId, tmp);
            }
        }
        for (List<Transaction> tx : friendTxMap.values()) {
            resultList.add(buildingAnalyticsResponseDto(requestDto, tx));

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

    private AbstractAnalyticsResponseDto buildingAnalyticsResponseDto(AnalyticsRequestDto requestDto, List<Transaction> transactions) throws AuthenticationException {
        User user = SecurityUtil.getCurrentUser().orElseThrow(AuthenticationException::new);

        Account account = accountRepository.findByIdAndUser(requestDto.getAccountId(), user)
                .orElseThrow(() -> new AccountNotFoundException(requestDto.getAccountId()));

        if (ObjectUtils.isEmpty(transactions)) {
            EmptyAnalyticsResponseDto.EmptyAnalyticsResponseDtoBuilder builder = EmptyAnalyticsResponseDto.builder();
            buildDate(requestDto, builder);
            builder.message("Данные не найдены");
            return builder.accountName(account.getName()).build();
        }
        if (requestDto.getType() != ArchiveStatsType.ALL) {
            SingleTypeAnalyticsResponseDto.SingleTypeAnalyticsResponseDtoBuilder builder = SingleTypeAnalyticsResponseDto.builder()
                    .accountName(account.getName())
                    .amount(transactions.stream()
                            .map(Transaction::getAmount)
                            .reduce(Float::sum)
                            .orElse(0F));
            buildDate(requestDto, builder);
            return builder.build();
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
                .accountName(account.getName());
        buildDate(requestDto, builder);
        return builder.build();
    }
//Метод для определения периода вывода аналитики
    private <T extends AbstractAnalyticsResponseDto.AbstractAnalyticsResponseDtoBuilder> void buildDate(AnalyticsRequestDto requestDto, T builder) {
        builder.dateFrom(ObjectUtils.isNotEmpty(requestDto.getDateFrom()) ?
                requestDto.getDateFrom() :
                LocalDateTime.now().minusDays(projectConfiguration.getBusiness().getArchivationPeriodDays()));

        builder.dateTo(ObjectUtils.isNotEmpty(requestDto.getDateTo()) ?
                requestDto.getDateTo() :
                LocalDateTime.now());
    }
}
