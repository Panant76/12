package by.vitstep.organizer.service;

import by.vitstep.organizer.exception.*;
import by.vitstep.organizer.model.dto.BillDto;
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
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@Validated
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
    public TxDto doTransact(@Valid CreateTxRequestDto request) {
        return doTransferTx(request);
    }

    private TxDto doTransferTx(CreateTxRequestDto request) {
        User currentUser = SecurityUtil.getCurrentUser()
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        Account sourceAccount = accountRepository.findByIdAndUser(request.getSourceAccountId(), currentUser)
                .orElseThrow(() -> new AccountNotFoundException(request.getSourceAccountId()));
        Account targetAccount = accountRepository.findById(request.getTargetAccountId())
                .orElseThrow(() -> new AccountNotFoundException(request.getTargetAccountId()));
        while (sourceAccount != targetAccount) {
            if (sourceAccount.getCurrency() == targetAccount.getCurrency()) {
                return txAndSave(sourceAccount, targetAccount, request);
            } else if (request.getIsAutoConverted()) {
                request.setAmount(exchangeService.exchange(request.getAmount(), sourceAccount.getCurrency(), targetAccount.getCurrency()));
                return txAndSave(sourceAccount, targetAccount, request);
            } else throw new TransactionException("Валюты счетов не совпадают");
        }
        throw new TransactionException("Перевод самому себе");
    }

    private TxDto txAndSave(Account sourceAccount, Account targetAccount, CreateTxRequestDto request) {
        return Optional.of(sourceAccount)
                .filter(acc -> acc.getAmount() >= request.getAmount())
                .map(account -> {
                    account.setAmount(account.getAmount() - request.getAmount());
                    targetAccount.setAmount(targetAccount.getAmount() + request.getAmount());
                    accountRepository.save(account);
                    accountRepository.save(targetAccount);

                    return mapper.toDto(createTransaction(request, getFriend(targetAccount), account, targetAccount));
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
                .map(user -> {
                    if (SecurityUtil.getCurrentUser()
                            .map(User::getId)
                            .stream()
                            .anyMatch(id -> !id.equals(user.getId()))) {
                        return friendRepository.findByUuidAndUser(user.getUuid(), SecurityUtil.getCurrentUser().get())
                                .orElseGet(() -> SecurityUtil.getCurrentUser()
                                        .map(self -> friendRepository.save(Friend.builder()
                                                .uuid(user.getUuid())
                                                .birthday(user.getBirthday())
                                                .contacts(user.getContacts())
                                                .name(user.getName())
                                                .user(self)
                                                .build()))
                                        .orElse(null));
                    }
                    return null;
                }).orElse(null);
    }

        public BillDto fillAccount (BillDto billDto){
            return accountRepository.findById(billDto.getId())
                    .map(account -> {
                        account.setAmount(account.getAmount() + exchangeService.exchange(billDto.getAmount(), billDto.getCurrency(), account.getCurrency()));
                        accountRepository.save(account);
                        Transaction tx = createTransaction(CreateTxRequestDto.builder()
                                .amount(billDto.getAmount())
                                .build(), null, null, account);
                        return billDto.builder()
                                .accountName(account.getName())
                                .currency(account.getCurrency())
                                .transactionDate(tx.getDateTime())
                                .id(tx.getId())
                                .build();
                    })
                    .orElseThrow(() -> new AccountNotFoundException(billDto.getId()));

        }
    }
