package by.vitstep.organizer.web;

import by.vitstep.organizer.model.dto.AccountDto;
import by.vitstep.organizer.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AccountController {
    AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(accountDto));
    }
//@PreAuthorize(value = "hasRole('ADMIN')")
    @GetMapping("/get")
public ResponseEntity<AccountDto> getAccountById(@RequestParam Long id){
        return ResponseEntity.ok(accountService.getAccountById(id));
    }
    @PatchMapping("/patch")
    public ResponseEntity<AccountDto> updateAccount(@RequestParam Long id,@RequestParam String name){
        return ResponseEntity.ok(accountService.updateAccount(id, name));
    }
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void  deleteAccount(@RequestParam Long id){
         accountService.deleteAccount(id);
    }
}
