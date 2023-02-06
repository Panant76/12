package by.vitstep.organizer.service;

import by.vitstep.organizer.config.ProjectConfiguration;
import by.vitstep.organizer.exception.AccountNotFoundException;
import by.vitstep.organizer.model.dto.AbstractArchiveStatsDto;
import by.vitstep.organizer.model.dto.enums.ArchiveStatsType;
import by.vitstep.organizer.model.entity.Account;
import by.vitstep.organizer.model.entity.Archive;
import by.vitstep.organizer.model.entity.Transaction;
import by.vitstep.organizer.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

import static by.vitstep.organizer.model.dto.enums.ArchiveStatsType.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArchivationService {
    TransactionRepository transactionRepository;
    AccountRepository accountRepository;
    ArchiveRepository archiveRepository;
    ProjectConfiguration projectConfiguration;
    FriendRepository friendRepository;
    UserRepository userRepository;

    private static Object apply(Archive archive) throws EnumConstantNotPresentException {
        if (ALL) {
            archive.getIncom();
            archive.getSpend();
        } else if (INCOME) {
            archive.getIncom();
        } else if (SPEND) {
            archive.getSpend();
        }
        else throw new EnumConstantNotPresentException(ArchiveStatsType.class,null);
        return archive;
    }

    @Async
    @Transactional
    @Scheduled(cron = "${project.business.scheduling.morning-cron}")
    public void archivate() {
        System.out.println("" + LocalDateTime.now() + "Процедура архивирования запущена!");
        LocalDateTime before = LocalDateTime.now().minusDays(projectConfiguration.getBusiness().getArchivationPeriodDays());
        transactionRepository.deleteAll(
                accountRepository.findAll()
                        .stream()
                        .collect(Collectors.toMap(Function.identity(), account -> transactionRepository.findByAccount(account, before)))
                        .entrySet()
                        .stream()
                        .map(entry -> {
                            Float spendAmount = entry.getValue()
                                    .stream()
                                    .filter(tx -> tx.getSourceAccount() != null)
                                    .filter(tx -> tx.getSourceAccount().getId().equals(entry.getKey().getId()))
                                    .map(Transaction::getAmount)
                                    .reduce(Float::sum)
                                    .orElse(0F);
                            Float incomeAmount = entry.getValue()
                                    .stream()
                                    .filter(tx -> tx.getTargetAccount().getId().equals(entry.getKey().getId()))
                                    .map(Transaction::getAmount)
                                    .reduce(Float::sum)
                                    .orElse(0F);
                            archiveRepository.save(Archive.builder()
                                    .account(entry.getKey())
                                    .spend(spendAmount)
                                    .incom(incomeAmount)
                                    .dateTo(before.toLocalDate())
                                    .build());
                            return entry.getValue();
                        })
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()));
        System.out.println("" + LocalDateTime.now() + "Процедура архивирования завершена!");
    }

//    @Transactional
//    @Scheduled
//    public void checkFriendUuid() {
//        friendRepository.findFriendUuidIsNull()
//                .stream()
//                .collect(Collectors.toMap(friend -> friend, friend -> friend.getContacts().getPhone()))
//                .forEach((friend, friendPhoneList) -> {
//                    userRepository.findAll()
//                            .stream()
//                            .collect(Collectors.toMap(user -> user, user -> user.getContacts().getPhone()))
//                            .forEach((user, userPhoneList) ->
//                                    userPhoneList.stream()
//                                            .forEach((phone) -> {
//                                                if (friendPhoneList.contains(phone)) {
//                                                    friend.setUuid(user.getUuid());
//                                                }
//                                            }));
//                });
//
//    }
    public AbstractArchiveStatsDto getStats(Long id, ArchiveStatsType type){
        Account account=accountRepository.findById(id).orElseThrow(()->new AccountNotFoundException(id));
        archiveRepository.findByAccount(account).map(ArchivationService::apply);
        return null; //???
    }

}
