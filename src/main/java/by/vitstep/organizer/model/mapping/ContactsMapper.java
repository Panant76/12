package by.vitstep.organizer.model.mapping;

import by.vitstep.organizer.model.dto.ContactsDto;
import by.vitstep.organizer.model.entity.Contacts;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactsMapper {
    Contacts toEntity(ContactsDto contactsDto);
    ContactsDto toDto(Contacts contacts);
}
