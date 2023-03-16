package by.vitstep.organizer.service.analytics;

import by.vitstep.organizer.config.ProjectConfiguration;
import by.vitstep.organizer.exception.AccountNotFoundException;
import by.vitstep.organizer.exception.BadRequestException;
import by.vitstep.organizer.model.dto.AllArchiveStatsDto;
import by.vitstep.organizer.model.dto.ArchiveStatsDto;
import by.vitstep.organizer.model.dto.SingleArchiveStatsDto;
import by.vitstep.organizer.model.dto.enums.ArchiveStatsType;
import by.vitstep.organizer.model.entity.Account;
import by.vitstep.organizer.model.entity.Archive;
import by.vitstep.organizer.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArchivationService {

    AccountRepository accountRepository;
    ArchiveRepository archiveRepository;
    ProjectConfiguration projectConfiguration;
    AccountArchivationProcessor accountArchivationProcessor;


    @Async
    @Transactional
    @Scheduled(cron = "${project.business.scheduling.morning-cron}")
    public void doArchiv() {
        System.out.println("" + LocalTime.now() + "Процедура архивирования запущена!");
        LocalDateTime before = LocalDateTime.now().minusDays(projectConfiguration.getBusiness().getArchivationPeriodDays());
        LocalDateTime dateFrom = archiveRepository
                .findLast()
                .map(Archive::getDateTo)
                .orElse(null);
        Long lastAccId = accountRepository.findCurrentSeq();
        List<Long> listId = new ArrayList<>();
        for (long i = 1; i <= lastAccId; i++) {
            listId.addAll(accountArchivationProcessor.processAccountArchive(i, before, dateFrom));
        }
        accountArchivationProcessor.delete(listId);
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
    public ArchiveStatsDto getStats(Long id, ArchiveStatsType type) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        return archiveRepository.findByAccount(account).map(archive -> {
            switch (type) {
                case ALL:
                    return AllArchiveStatsDto.builder()
                            .accountName(account.getName())//НОМЕР СЧЕТА
                            .dateFrom(LocalDate.from(archive.getDateFrom()))
                            .dateTo(LocalDate.from(archive.getDateTo()))
                            .income(archive.getIncom())
                            .spend(archive.getSpend())
                            .build();

                case INCOME:
                    return SingleArchiveStatsDto.builder()
                            .accountName(account.getName())
                            .dateFrom(LocalDate.from(archive.getDateFrom()))
                            .dateTo(LocalDate.from(archive.getDateTo()))
                            .amount(archive.getIncom())
                            .build();

                case SPEND:
                    return SingleArchiveStatsDto.builder()
                            .accountName(account.getName())
                            .dateFrom(LocalDate.from(archive.getDateFrom()))
                            .dateTo(LocalDate.from(archive.getDateTo()))
                            .amount(archive.getSpend())
                            .build();

                default:
                    throw new BadRequestException("Ошибка сериализации");
            }
        }).orElseGet(() -> ArchiveStatsDto.builder()
                .accountName(account.getName())
                .build());
    }

    public ArchiveStatsDto getStats1(Long id, ArchiveStatsType type) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        return archiveRepository.findByAccount(account).map(archive -> {
            ArchiveStatsDto.builder()
                    .accountName(account.getName())//НОМЕР СЧЕТА
                    .dateFrom(LocalDate.from(archive.getDateFrom()))
                    .dateTo(LocalDate.from(archive.getDateTo()))
                    .build();
            switch (type) {
                case ALL:
                    return AllArchiveStatsDto.builder()
                            .income(archive.getIncom())
                            .spend(archive.getSpend())
                            .build();

                case INCOME:
                    return SingleArchiveStatsDto.builder()
                            .amount(archive.getIncom())
                            .build();

                case SPEND:
                    return SingleArchiveStatsDto.builder()
                            .amount(archive.getSpend())
                            .build();

                default:
                    throw new BadRequestException("Ошибка сериализации");
            }
        }).orElseGet(() -> ArchiveStatsDto.builder()
                .accountName(account.getName())
                .build());
    }

}
