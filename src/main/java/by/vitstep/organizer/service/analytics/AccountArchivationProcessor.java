package by.vitstep.organizer.service.analytics;

import by.vitstep.organizer.model.entity.Archive;
import by.vitstep.organizer.model.entity.Transaction;
import by.vitstep.organizer.repository.AccountRepository;
import by.vitstep.organizer.repository.ArchiveRepository;
import by.vitstep.organizer.repository.TransactionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AccountArchivationProcessor {
    TransactionRepository   transactionRepository;
    AccountRepository accountRepository;
    ArchiveRepository archiveRepository;
    public List<Long> processAccountArchive(Long accId, LocalDateTime before, LocalDateTime dateFrom){
        return accountRepository.findById(accId).map(account -> {
            List<Transaction> listTransaction =transactionRepository.findByAccount(account,before);
            Float spentAmount=listTransaction
                    .stream()
                    .filter(tx->tx.getSourceAccount()!=null)
                    .filter(tx->tx.getSourceAccount().getId().equals(account.getId()))
                    .map(Transaction::getAmount)
                    .reduce(Float::sum)
                    .orElse(0F);
            Float incomeAmount=listTransaction
                    .stream()
                    .filter(tx -> tx.getTargetAccount().getId().equals(account.getId()))
                    .map(Transaction::getAmount)
                    .reduce(Float::sum)
                    .orElse(0F);
            archiveRepository.deleteByAccId(account.getId());
            archiveRepository.save(Archive.builder()
                    .account(account)
                    .spend(spentAmount)
                    .incom(incomeAmount)
                    .dateTo(before)
                    .dateFrom(dateFrom)
                    .build());
            return listTransaction.stream()
                    .map(transaction -> transaction.getId())
                    .collect(Collectors.toList());

        }).orElse(List.of());
    }
    public void delete(List<Long> listId){
        transactionRepository.deleteAllById(listId);
    }
}
