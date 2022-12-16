package by.vitstep.organizer.service;

import by.vitstep.organizer.exception.BadRequestException;
import by.vitstep.organizer.exception.TransactionNotFoundException;
import by.vitstep.organizer.exception.UnsupportedTransactionException;
import by.vitstep.organizer.model.dto.CreateTxRequestDto;
import by.vitstep.organizer.model.dto.TxDto;
import by.vitstep.organizer.model.mapping.TransactionMapper;
import by.vitstep.organizer.repository.AccountRepository;
import by.vitstep.organizer.repository.FriendRepository;
import by.vitstep.organizer.repository.TransactionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionService {
    TransactionMapper transactionMapper;
    TransactionRepository transactionRepository;
    FriendRepository friendRepository;
    AccountRepository accountRepository;

    public TxDto getTx(final Long id) {
        return transactionRepository.findById(id)
                .map(transactionMapper::toDto)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }
    @Transactional
    public TxDto doTransact(CreateTxRequestDto request){
        if(request.getType()==null){
            throw new UnsupportedTransactionException("Не заполнен тип транзакции");
        }
        if(request.getAmount()==null){
            throw  new BadRequestException("Не указана сумма транзакции");
        }
    }

}
