package by.vitstep.organizer.service;

import by.vitstep.organizer.exception.UserNotFoundException;
import by.vitstep.organizer.model.dto.FriendDto;
import by.vitstep.organizer.model.entity.Friend;
import by.vitstep.organizer.model.entity.User;
import by.vitstep.organizer.model.mapping.FriendMapper;
import by.vitstep.organizer.repository.FriendRepository;
import by.vitstep.organizer.repository.UserRepository;
import by.vitstep.organizer.utils.SecurityUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FriendService {
    FriendRepository friendRepository;
    FriendMapper friendMapper;
    UserRepository userRepository;

    @Transactional
    public FriendDto createFriend(FriendDto friendDto) {
        Friend friend = friendMapper.toEntity(friendDto);
        log.debug("Входящий запрос на создания объекта друга: {}", friendDto);
        Optional<User> userOptional = userRepository.findByPhone(friend.getContacts().getPhone());
        userOptional.ifPresent(user -> friend.setUuid(user.getUuid()));
        User currentUser = SecurityUtil.getCurrentUser().orElseThrow(() -> new UserNotFoundException("Ошибка авторизации"));
        friend.setUser(currentUser);
        friendRepository.save(friend);
        return friendMapper.toDto(friend);
    }
    @Transactional
    public void friendUpdateWithUuid(Long userId){
        userRepository.findById(userId)
                .map(user->{
                    friendRepository.findByPhone(user.getContacts().getPhone()).forEach(friend->{
                        friend.setUuid(user.getUuid());
                        friendRepository.save(friend);
                    });
                    return user;
                })
                .orElseThrow(()->new UserNotFoundException(userId));

    }
}


