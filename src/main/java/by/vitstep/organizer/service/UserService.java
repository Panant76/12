package by.vitstep.organizer.service;

import by.vitstep.organizer.config.ProjectConfiguration;
import by.vitstep.organizer.exception.UserAlreadyExistException;
import by.vitstep.organizer.exception.UserNotFoundException;
import by.vitstep.organizer.model.dto.UserDto;
import by.vitstep.organizer.model.entity.User;
import by.vitstep.organizer.model.mapping.UserMapper;
import by.vitstep.organizer.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService {
    UserRepository userRepository;

    UserMapper userMapper;
    ProjectConfiguration projectConfiguration;

    public UserDto createUser(UserDto user) {
        User userToSave = userMapper.toEntity(user);
        try {
            userRepository.save(userToSave);
        } catch (Exception ex) {
            throw new UserAlreadyExistException(String.format("Логин %s уже занят", user.getLogin()));
        }
        return userMapper.toDto(userToSave);
    }
    public UserDto getUser(Long id){
        return userMapper.toDto(userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id)));


    }
}
