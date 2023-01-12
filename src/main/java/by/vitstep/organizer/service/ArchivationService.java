package by.vitstep.organizer.service;

import by.vitstep.organizer.config.ProjectConfiguration;
import by.vitstep.organizer.model.entity.Archive;
import by.vitstep.organizer.model.entity.Transaction;
import by.vitstep.organizer.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ArchivationService {
    TransactionRepository transactionRepository;
    AccountRepository accountRepository;
    ArchiveRepository archiveRepository;
    ProjectConfiguration projectConfiguration;
    UserRepository userRepository;

    public void archivate(){
        LocalDateTime before=LocalDateTime.now().minusDays(projectConfiguration.getBusiness().getArchivationPeriodDays());
        accountRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Function.identity(),account-> transactionRepository.findByAccount(account,before)))
                .forEach((key,value)->{
                    Float spendAmount=value
                            .stream()
                            .filter(tx->tx.getSourceAccount().getId().equals(key.getId()))
                            .map(Transaction::getAmount)
                            .reduce(Float::sum)
                            .orElse(0F);
                    Float incomeAmount=value
                            .stream()
                            .filter(tx->tx.getTargetAccount().getId().equals(key.getId()))
                            .map(Transaction::getAmount)
                            .reduce(Float::sum)
                            .orElse(0F);
                    archiveRepository.save(Archive.builder()
                            .account(key)
                            .spend(spendAmount)
                            .incom(incomeAmount)
                            .build());
                });
    }
    public void setUserUuid(){
        userRepository.findAll()
                .stream()
                .collect(Collectors.toMap(us-> us.getContacts().getPhone(),us->us))
                .forEach((key,value);

    }
}
