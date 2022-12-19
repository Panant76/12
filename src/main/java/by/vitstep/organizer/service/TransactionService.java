package by.vitstep.organizer.service;

import by.vitstep.organizer.exception.*;
import by.vitstep.organizer.model.dto.CreateTxRequestDto;
import by.vitstep.organizer.model.dto.TxDto;
import by.vitstep.organizer.model.entity.Account;
import by.vitstep.organizer.model.entity.Friend;
import by.vitstep.organizer.model.entity.Transaction;
import by.vitstep.organizer.model.mapping.TransactionMapper;
import by.vitstep.organizer.repository.AccountRepository;
import by.vitstep.organizer.repository.FriendRepository;
import by.vitstep.organizer.repository.TransactionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (request.getType() == null) {
            throw new UnsupportedTransactionException("Не заполнен тип транзакции");
        }
        if (request.getAmount() == null) {
            throw new BadRequestException("Не указана сумма транзакции");
        }
        switch (request.getType()) {
            case INCOME:
                return doIncomeTx(request);
            case OUTCOME:
                return doOutcomeTx(request);
            case TRANSFER:
                return doTransferTx(request);
        }
        return null;

    }

    private TxDto doIncomeTx(CreateTxRequestDto request) {
        final Friend friend = getFriend(request.getFriendId());
        return Optional.ofNullable(request.getTargetAccountId())
                .flatMap(accountRepository::findById)
                .map(account -> {
                    account.setAmount(account.getAmount() + request.getAmount());
                    accountRepository.save(account);
                    return mapper.toDto(createTransaction(request, friend, account));
                })
                .orElseThrow(() -> new AccountNotFoundException(request.getTargetAccountId()));
    }

    private TxDto doOutcomeTx(CreateTxRequestDto request) {
        final Friend friend = getFriend(request.getFriendId());
        final Account account = Optional.ofNullable(request.getSourceAccountId())
                .flatMap(accountRepository::findById)
                .orElseThrow(() -> new AccountNotFoundException(request.getSourceAccountId()));
        return Optional.of(account)
                .filter(acc -> acc.getAmount() >= request.getAmount())
                .map(acc -> {
                    acc.setAmount(acc.getAmount() - request.getAmount());
                    accountRepository.save(acc);
                    return mapper.toDto(createTransaction(request, friend, acc));
                })
                .orElseThrow(() -> new NotEnoughFoundException(account.getName()));
    }

    private TxDto doTransferTx(CreateTxRequestDto request) {
        Account sourceAccount = Optional.ofNullable(request.getSourceAccountId())
                .flatMap(id -> accountRepository.findById(request.getSourceAccountId()))
                .orElseThrow(() -> new AccountNotFoundException(request.getSourceAccountId()));
        Account targetAccount = Optional.ofNullable(request.getTargetAccountId())
                .flatMap(id -> accountRepository.findById(request.getTargetAccountId()))
                .orElseThrow(() -> new AccountNotFoundException(request.getTargetAccountId()));
        return Optional.of(sourceAccount)
                .filter(acc -> acc.getAmount() >= request.getAmount())
                .map(account -> {
                    account.setAmount(account.getAmount()- request.getAmount());
                    targetAccount.setAmount(targetAccount.getAmount()+ request.getAmount());
                    accountRepository.save(account);
                    accountRepository.save(targetAccount);
                    return mapper.toDto(createTransaction(request,null,account,targetAccount));
                })
                .orElseThrow(()-> new NotEnoughFoundException(sourceAccount.getName()));

    }

    private Transaction createTransaction(CreateTxRequestDto request, Friend friend, Account account) {
        return transactionRepository.save(Transaction
                .builder()
                .transactionType(request.getType())
                .amount(request.getAmount())
                .account(account)
                .friend(friend)
                .build());

    }

    private Friend getFriend(Long id) {
        return Optional.ofNullable(id)
                .flatMap(friendRepository::findById)
                .orElseThrow(() -> new FriendNotFoundException("Не верный идентификатор друга"));
    }


}
