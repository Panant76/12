package by.vitstep.organizer.service;

import by.vitstep.organizer.model.dto.RegistrationRequest;
import by.vitstep.organizer.model.dto.UserDto;
import by.vitstep.organizer.model.entity.Authority;
import by.vitstep.organizer.model.entity.Contacts;
import by.vitstep.organizer.model.entity.User;
import by.vitstep.organizer.model.mapping.UserMapper;
import by.vitstep.organizer.repository.UserRepository;
import by.vitstep.organizer.service.handler.UserCreationHandler;
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
    FriendService friendService;
    UserCreationHandler userCreationHandler;
    AccountService accountService;

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
                .email(Optional.ofNullable(request.getEmail())
                        .map(List::of)
                        .orElse(List.of()))
                .build());
        if (request.getCreateAccounts()) accountService.createAllAccount(request.getName());
        final UserDto result = userMapper.toDto(userCreationHandler.doCreate(userToSave));
        friendService.friendUpdateWithUuid(result.getId());
        return result;
    }    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepository.findByLogin(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Не верное имя пользователя или пароль"));
    }

    public Optional<User> getById(final Long id) {
        return userRepository.findById(id);
    }

}
