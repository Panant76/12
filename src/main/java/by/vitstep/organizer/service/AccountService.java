package by.vitstep.organizer.service;

import by.vitstep.organizer.exception.AccountAlreadyExistException;
import by.vitstep.organizer.exception.AccountNotFoundException;
import by.vitstep.organizer.model.dto.AccountDto;
import by.vitstep.organizer.model.entity.Account;
import by.vitstep.organizer.model.entity.User;
import by.vitstep.organizer.model.mapping.AccountMapper;
import by.vitstep.organizer.repository.AccountRepository;
import by.vitstep.organizer.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AccountService {
    AccountRepository accountRepository;
    AccountMapper accountMapper;

    @Transactional
    public AccountDto createAccount(AccountDto accountDto) {
        Account accountToSave = accountMapper.toEntity(accountDto);
        accountToSave.setUser((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
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

    @Transactional
    public AccountDto updateAccount(Long id, String name) {
        return accountRepository
                .findById(id)
                .map((acName) -> {
                    acName.setName(name);
                    return accountRepository.save(acName);
                })
                .map(accountMapper::toDto)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Transactional
    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new AccountNotFoundException(id);
        }
        accountRepository.deleteById(id);

    }
}
