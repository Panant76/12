package by.vitstep.organizer.model.mapping;

import by.vitstep.organizer.model.dto.FriendDto;
import by.vitstep.organizer.model.entity.Friend;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FriendMapper {
    Friend toEntity(FriendDto friendDto);
    FriendDto toDto(Friend friend);
}
