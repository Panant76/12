package by.vitstep.organizer.model.mapping;

import by.vitstep.organizer.model.dto.TxDto;
import by.vitstep.organizer.model.entity.Transaction;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring",injectionStrategy = InjectionStrategy.CONSTRUCTOR,imports = FriendMapper.class)
public interface TransactionMapper {
    @Mapping(target = "sourceAccountId",source = "tx.sourceAccount.id")
    @Mapping(target = "sourceAccountName",source = "tx.sourceAccount.name")
    @Mapping(target = "targetAccountId",source = "tx.targetAccount.id")
    @Mapping(target = "targetAccountName",source = "tx.targetAccount.name")
    TxDto toDto(Transaction tx);
}
