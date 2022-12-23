package by.vitstep.organizer.service;

import by.vitstep.organizer.exception.UserAlreadyExistException;
import by.vitstep.organizer.model.dto.RegistrationRequest;
import by.vitstep.organizer.model.dto.UserDto;
import by.vitstep.organizer.model.entity.Authority;
import by.vitstep.organizer.model.entity.User;
import by.vitstep.organizer.model.mapping.UserMapper;
import by.vitstep.organizer.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    UserRepository userRepository;
    UserMapper userMapper;


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
        return create(userToSave);

    }
    public UserDto create(User userToSave){
        try {
            userRepository.save(userToSave);
        }catch(Exception ex){
            throw new UserAlreadyExistException(String.format("Логин %s уже занят", userToSave.getLogin()));
        }
        return userMapper.toDto(userToSave);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username)
                .orElseThrow(()->new UsernameNotFoundException("Не верное имя пользователя или пароль"));
    }
    public Optional<User> getById(final Long id){
        return userRepository.findById(id);
    }
}
