package by.vitstep.organizer.service;

import by.vitstep.organizer.exception.UserAlreadyExistException;
import by.vitstep.organizer.model.dto.RegistrationRequest;
import by.vitstep.organizer.model.dto.UserDto;
import by.vitstep.organizer.model.entity.Authority;
import by.vitstep.organizer.model.entity.Contacts;
import by.vitstep.organizer.model.entity.User;
import by.vitstep.organizer.model.mapping.UserMapper;
import by.vitstep.organizer.repository.FriendRepository;
import by.vitstep.organizer.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    UserRepository userRepository;
    UserMapper userMapper;
    FriendRepository friendRepository;

@Transactional
    public UserDto createUser(RegistrationRequest request) {
        User userToSave = userMapper.registrationToEntity(request);
        userToSave.setAuthorities(request
                .getRoles()
                .stream()
                .map(roles -> Authority
                        .builder()
                        .authority(roles)
                        .user(userToSave)
                        .build())
                .collect(Collectors.toList()));
        userToSave.setContacts(Contacts.builder()
                .phone(request.getPhone())
                .email(List.of(request.getEmail()))
                .build());
        return create(userToSave);

    }
    public UserDto create(User userToSave){
        try {
            userRepository.save(userToSave);
//            userRepository.findById(userToSave.getId())
//            .map(user -> {
//                friendRepository.findByPhone(user.getContacts().getPhone())
//                        .forEach(friend -> {
//                            friend.setUuid(user.getUuid());
//                            friendRepository.save(friend);
//                        });
//                return user;
//            })
//                    .orElseThrow(()->new UserNotFoundException(userToSave.getId()));
        }catch(Exception ex){
            log.error(ex.getMessage(), ex);
            throw new UserAlreadyExistException(String.format("Логин %s уже занят", userToSave.getLogin()));
        }
        return userMapper.toDto(userToSave);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepository.findByLogin(userName)
                .orElseThrow(()->new UsernameNotFoundException("Не верное имя пользователя или пароль"));
    }
    public Optional<User> getById(final Long id){
        return userRepository.findById(id);
    }

}
