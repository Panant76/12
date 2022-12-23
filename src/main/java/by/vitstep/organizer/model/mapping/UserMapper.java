package by.vitstep.organizer.model.mapping;

import by.vitstep.organizer.model.dto.RegistrationRequest;
import by.vitstep.organizer.model.dto.UserDto;
import by.vitstep.organizer.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDto userDto);
    UserDto toDto(User user);
    User registrationToEntity(RegistrationRequest request);

}
