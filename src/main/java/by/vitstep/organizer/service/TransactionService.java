package by.vitstep.organizer.service;

import by.vitstep.organizer.exception.*;
import by.vitstep.organizer.model.dto.CreateTxRequestDto;
import by.vitstep.organizer.model.dto.TxDto;
import by.vitstep.organizer.model.entity.Account;
import by.vitstep.organizer.model.entity.Friend;
import by.vitstep.organizer.model.entity.Transaction;
import by.vitstep.organizer.model.entity.User;
import by.vitstep.organizer.model.mapping.TransactionMapper;
import by.vitstep.organizer.repository.AccountRepository;
import by.vitstep.organizer.repository.FriendRepository;
import by.vitstep.organizer.repository.TransactionRepository;
import by.vitstep.organizer.utils.SecurityUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionService {
    TransactionMapper mapper;
    TransactionRepository transactionRepository;
    FriendRepository friendRepository;
    AccountRepository accountRepository;
    CurrencyExchangeService exchangeService;

    public TxDto getTx(final Long id) {

        return transactionRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    @Transactional
    public TxDto doTransact(CreateTxRequestDto request) {
        if (request.getAmount() == null) {
            throw new BadRequestException("Не указана сумма транзакции");

        }
        return doTransferTx(request);
    }

    private TxDto doTransferTx(CreateTxRequestDto request) {
        User currentUser = SecurityUtil.getCurrentUser()
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        Account sourceAccount = Optional.ofNullable(request.getSourceAccountId())
                .flatMap(id -> accountRepository.findByIdAndUser(request.getSourceAccountId(), currentUser))
                .orElseThrow(() -> new AccountNotFoundException(request.getSourceAccountId()));
        Account targetAccount = Optional.ofNullable(request.getTargetAccountId())
                .flatMap(id -> accountRepository.findById(request.getTargetAccountId()))
                .orElseThrow(() -> new AccountNotFoundException(request.getTargetAccountId()));
        if (sourceAccount.getCurrency() == targetAccount.getCurrency()) {
            return txAndSave(sourceAccount, targetAccount, request);
        } else if (request.getIsAutoConverted()) {
            request.setAmount(exchangeService.exchange(request.getAmount(), sourceAccount.getCurrency(), targetAccount.getCurrency()));
            return txAndSave(sourceAccount, targetAccount, request);
        } else throw new TransactionException("Валюты счетов не совпадают");

    }

    private TxDto txAndSave(Account sourceAccount, Account targetAccount, CreateTxRequestDto request) {
        return Optional.of(sourceAccount)
                .filter(acc -> acc.getAmount() >= request.getAmount())
                .map(account -> {
                    account.setAmount(account.getAmount() - request.getAmount());
                    targetAccount.setAmount(targetAccount.getAmount() + request.getAmount());
                    accountRepository.save(account);
                    accountRepository.save(targetAccount);

                    return mapper.toDto(createTransaction(request, getFriend(targetAccount), sourceAccount, targetAccount));
                })
                .orElseThrow(() -> new NotEnoughFoundException(sourceAccount.getName()));
    }

    private Transaction createTransaction(CreateTxRequestDto request, Friend friend, Account sourceAccount, Account targetAccount) {
        return transactionRepository.save(Transaction
                .builder()
                .amount(request.getAmount())
                .sourceAccount(sourceAccount)
                .targetAccount(targetAccount)
                .dateTime(LocalDateTime.now())
                .friend(friend)
                .build());

    }

    private Friend getFriend(Account targetAccount) {
        return Optional.ofNullable(targetAccount.getUser())
                .flatMap(user -> {
                    if (SecurityUtil.getCurrentUser()
                            .map(User::getId)
                            .stream()
                            .anyMatch(id -> id.equals(user.getId()))) {
                        User self = SecurityUtil.getCurrentUser().get();
                        return Optional.of(friendRepository.findByUuidAndUser(user.getUuid(), self)
                                .orElseGet(() -> friendRepository.save(Friend.builder()
                                        .uuid(self.getUuid())
                                        .birthday(self.getBirthday())
                                        .contacts(self.getContacts())
                                        .user(self)
                                        .build())));
                    }
                    return friendRepository.findByUuidAndUser(user.getUuid(), SecurityUtil.getCurrentUser().get());
                })
                .orElse(null);
    }


}
