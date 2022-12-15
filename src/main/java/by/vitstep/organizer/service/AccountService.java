package by.vitstep.organizer.service;

import by.vitstep.organizer.exception.AccountAlreadyExistException;
import by.vitstep.organizer.exception.AccountNotFoundException;
import by.vitstep.organizer.exception.UserNotFoundException;
import by.vitstep.organizer.model.dto.AccountDto;
import by.vitstep.organizer.model.entity.Account;
import by.vitstep.organizer.model.mapping.AccountMapper;
import by.vitstep.organizer.repository.AccountRepository;
import by.vitstep.organizer.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AccountService {
    AccountRepository accountRepository;
    AccountMapper accountMapper;
    UserRepository userRepository;

    public AccountDto createAccount(AccountDto accountDto) {
        Account accountToSave = accountMapper.toEntity(accountDto);
        accountToSave.setUser(userRepository.findById(1L).orElseThrow(() -> new UserNotFoundException(1L)));
        try {
            accountRepository.save(accountToSave);
        } catch (Exception ex) {
            throw new AccountAlreadyExistException(accountDto.getName());
        }
        return accountMapper.toDto(accountToSave);
    }

    public AccountDto getAccountById(Long id) {
        return accountMapper
                .toDto(accountRepository
                        .findById(id)
                        .orElseThrow(() -> new AccountNotFoundException(id)));
    }

    public AccountDto updateAccount(Long id, String name) {
        return accountMapper
                .toDto(accountRepository
                        .findById(id)
                        .map((acName) -> {
                            acName.setName(name);
                            return acName;
                        })
                        .orElseThrow(() -> new AccountNotFoundException(id)));
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);

    }
}
