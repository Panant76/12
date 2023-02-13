package by.vitstep.organizer.service;

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
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AnalyticsService {
    TransactionRepository transactionRepository;
    EntityManager entityManager;
    AccountRepository accountRepository;
    ProjectConfiguration projectConfiguration;

    @Transactional
    public AbstractAnalyticsResponseDto getTxAnalytics(AnalyticsRequestDto requestDto) throws AuthenticationException {
        User user = SecurityUtil.getCurrentUser().orElseThrow(AuthenticationException::new);
        Account account = accountRepository.findByIdAndUser(requestDto.getAccountId(), user)
                .orElseThrow(() -> new AccountNotFoundException(requestDto.getAccountId()));
        String query = "select * from transaction where ";
        if (requestDto.getType() == ArchiveStatsType.INCOME) {
            query = query.concat("target_account= ").concat(requestDto.getAccountId().toString().concat(" "));
        }
        if (requestDto.getType() == ArchiveStatsType.SPEND) {
            query = query.concat("source_account= ").concat(requestDto.getAccountId().toString().concat(" "));
        }
        if (ObjectUtils.isNotEmpty(requestDto.getDateFrom())) {
            query = query.concat("and date_time> ").concat(requestDto.getDateFrom().toString().concat(" "));
        }
        if (ObjectUtils.isNotEmpty(requestDto.getDateTo())) {
            query = query.concat("and date_time< ").concat(requestDto.getDateTo().toString().concat(" "));
        }
        if (ObjectUtils.isNotEmpty(requestDto.getLessThan())) {
            query = query.concat("and amount< ").concat(requestDto.getLessThan().toString().concat(" "));
        }
        if (ObjectUtils.isNotEmpty(requestDto.getGreaterThan())) {
            query = query.concat("and amount> ").concat(requestDto.getGreaterThan().toString().concat(" "));
        }
        query = query.concat(";");
        Query entityQuery = entityManager.createNativeQuery(query, Transaction.class);
        List<Transaction> transactions = entityQuery.getResultList();

        if (ObjectUtils.isEmpty(transactions)) {
            EmptyAnalyticsResponseDto.EmptyAnalyticsResponseDtoBuilder builder = EmptyAnalyticsResponseDto.builder();
            buildDate(requestDto, builder);
            builder.message("Данные не найдены");
            return builder.accountName(account.getName()).build();
        }
        if (requestDto.getType() != ArchiveStatsType.ALL) {
            SingleTypeAnalyticsResponseDto.SingleTypeAnalyticsResponseDtoBuilder builder = SingleTypeAnalyticsResponseDto.builder()
                    .accountName(account.getName())
                    .amount(transactions.stream().map(Transaction::getAmount).reduce(Float::sum).orElse(0F));
            buildDate(requestDto, builder);
            return builder.build();
        }
        MultipleTypesAnalyticsResponseDto.
                MultipleTypesAnalyticsResponseDtoBuilder builder = MultipleTypesAnalyticsResponseDto.builder()
                .incomeAmount(transactions.stream().map((tx) -> tx.getTargetAccount().getAmount()).reduce(Float::sum).orElse(0f))
                .spendAmount(transactions.stream().map((tx) -> tx.getSourceAccount().getAmount()).reduce(Float::sum).orElse(0F));
       buildDate(requestDto, builder);
        builder.build();

        return SingleTypeAnalyticsResponseDto.builder()
                .amount(transactions.stream().map(Transaction::getAmount).reduce(Float::sum).orElse(0F))
                .accountName(transactions.stream().map(tx -> tx.getTargetAccount().getName()).findAny().orElse(null))
                .build();

    }

    private <T extends AbstractAnalyticsResponseDto.AbstractAnalyticsResponseDtoBuilder> void buildDate(AnalyticsRequestDto requestDto, T builder) {
        builder.dateFrom(ObjectUtils.isNotEmpty(requestDto.getDateFrom()) ?
                requestDto.getDateFrom() :
                LocalDateTime.now().minusDays(projectConfiguration.getBusiness().getArchivationPeriodDays()));

        builder.dateTo(ObjectUtils.isNotEmpty(requestDto.getDateTo()) ?
                requestDto.getDateTo() :
                LocalDateTime.now());
    }
}
