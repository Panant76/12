package by.vitstep.organizer.service;

import by.vitstep.organizer.exception.FriendAlreadyExistException;
import by.vitstep.organizer.model.dto.FriendDto;
import by.vitstep.organizer.model.entity.Friend;
import by.vitstep.organizer.model.mapping.FriendMapper;
import by.vitstep.organizer.repository.FriendRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FriendService {
    FriendRepository friendRepository;
    FriendMapper friendMapper;

    public FriendDto createFriend(FriendDto friend) {
        Friend friendToSave = friendMapper.toEntity(friend);
        try {
            friendRepository.save(friendToSave);
        } catch (Exception ex) {
            throw new FriendAlreadyExistException("!!!");
        }
        return friendMapper.toDto(friendToSave);
    }
}

