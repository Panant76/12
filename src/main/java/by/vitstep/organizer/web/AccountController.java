package by.vitstep.organizer.web;

import by.vitstep.organizer.model.dto.AccountDto;
import by.vitstep.organizer.model.dto.ArchiveStatsDto;
import by.vitstep.organizer.model.dto.BillDto;
import by.vitstep.organizer.model.dto.ananlytics.AbstractAnalyticsResponseDto;
import by.vitstep.organizer.model.dto.ananlytics.AnalyticsRequestDto;
import by.vitstep.organizer.model.dto.enums.ArchiveStatsType;
import by.vitstep.organizer.service.AccountService;
import by.vitstep.organizer.service.AnalyticsService;
import by.vitstep.organizer.service.ArchivationService;
import by.vitstep.organizer.service.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/account")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AccountController {
    AccountService accountService;
    ArchivationService archivationService;
    TransactionService transactionService;
    AnalyticsService analyticsService;

    @PostMapping("/create")
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(accountDto));
    }

    @GetMapping("/get")
    public ResponseEntity<AccountDto> getAccountById(@RequestParam Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @PatchMapping("/patch")
    public ResponseEntity<AccountDto> updateAccount(@RequestParam Long id, @RequestParam String name) {
        return ResponseEntity.ok(accountService.updateAccount(id, name));
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@RequestParam Long id) {
        accountService.deleteAccount(id);
    }

    @GetMapping("/get-archive-stats")
    public ResponseEntity<? extends ArchiveStatsDto> getArchiveStats(@RequestParam Long id, @RequestParam ArchiveStatsType type) {
        return ResponseEntity.ok(archivationService.getStats(id, type));
    }

    @PatchMapping("/fill")
    public ResponseEntity<BillDto> fill(@RequestBody @Valid BillDto billDto) {
        return ResponseEntity.ok(transactionService.fillAccount(billDto));
    }

    @GetMapping("/get-analytics")
    public ResponseEntity<? extends AbstractAnalyticsResponseDto> getAnalytics(@RequestParam Long accountId,
                                                                               @RequestParam ArchiveStatsType type,
                                                                               @RequestParam(required = false) LocalDateTime dateFrom,
                                                                               @RequestParam(required = false) LocalDateTime dateTo,
                                                                               @RequestParam(required = false) Float gtThan,
                                                                               @RequestParam(required = false) Float lsThan) throws AuthenticationException {
        return ResponseEntity.ok(analyticsService.getTxAnalytics(AnalyticsRequestDto.builder()
                .type(type)
                .accountId(accountId)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .greaterThan(gtThan)
                .lessThan(lsThan)
                .build()));
    }
}
