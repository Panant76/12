package by.vitstep.organizer.model.mapping;

import by.vitstep.organizer.model.dto.TxDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import javax.transaction.Transaction;

@Mapper(componentModel = "spring",injectionStrategy = InjectionStrategy.CONSTRUCTOR,imports = FriendMapper.class)
public interface TransactionMapper {
    @Mapping(target = "accountId",source = "tx.account.id")
    @Mapping(target = "accountName",source = "tx.account.name")
    TxDto toDto(Transaction tx);
}
