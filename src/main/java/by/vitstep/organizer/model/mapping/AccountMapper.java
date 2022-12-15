package by.vitstep.organizer.model.mapping;

import by.vitstep.organizer.model.dto.AccountDto;
import by.vitstep.organizer.model.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface AccountMapper {
    Account toEntity(AccountDto accountDto);
    AccountDto toDto (Account account);
}
