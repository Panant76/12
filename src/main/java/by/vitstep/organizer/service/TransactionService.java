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

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionService {
    TransactionMapper mapper;
    TransactionRepository transactionRepository;
    FriendRepository friendRepository;
    AccountRepository accountRepository;

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
                .orElseThrow(()->new UserNotFoundException("ПОльзователь не найден"));
        Account sourceAccount = Optional.ofNullable(request.getSourceAccountId())
                .flatMap(id -> accountRepository.findByIdAndUser(request.getSourceAccountId(), currentUser))
                .orElseThrow(() -> new AccountNotFoundException(request.getSourceAccountId()));
        Account targetAccount = Optional.ofNullable(request.getTargetAccountId())
                .flatMap(id -> accountRepository.findById(request.getTargetAccountId()))
                .orElseThrow(() -> new AccountNotFoundException(request.getTargetAccountId()));
        return Optional.of(sourceAccount)
                .filter(acc -> acc.getAmount() >= request.getAmount())
                .map(account -> {
                    account.setAmount(account.getAmount() - request.getAmount());
                    targetAccount.setAmount(targetAccount.getAmount() + request.getAmount());
                    accountRepository.save(account);
                    accountRepository.save(targetAccount);
                    return Optional.ofNullable(request.getFriendId())
                            .flatMap(friendRepository::findById)
                            .map(friend -> mapper.toDto(createTransaction(request, friend, sourceAccount, targetAccount)))
                            .orElseGet(() -> mapper.toDto(createTransaction(request, null, sourceAccount, targetAccount)));
                })
                .orElseThrow(() -> new NotEnoughFoundException(sourceAccount.getName()));

    }

    private Transaction createTransaction(CreateTxRequestDto request, Friend friend, Account sourseAccount, Account targetAccount) {
        return transactionRepository.save(Transaction
                .builder()
                .amount(request.getAmount())
                .sourceAccount(sourseAccount)
                .targetAccount(targetAccount)
                .dateTime(LocalDateTime.now())
                .friend(friend)
                .build());

    }

    private Friend getFriend(Long id) {
        return Optional.ofNullable(id)
                .flatMap(friendRepository::findById)
                .orElseThrow(() -> new FriendNotFoundException("Не верный идентификатор друга"));
    }


}
