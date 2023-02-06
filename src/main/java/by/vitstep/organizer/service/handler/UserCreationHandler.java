package by.vitstep.organizer.service.handler;

import by.vitstep.organizer.exception.UserAlreadyExistException;
import by.vitstep.organizer.model.entity.User;
import by.vitstep.organizer.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserCreationHandler {
    UserRepository userRepository;
    EntityManager entityManager;

    public User doCreate(final User userToSave) {
        try {
            User user = userRepository.saveAndFlush(userToSave);
            entityManager.clear();
            return user;
        } catch (Exception ex) {
            throw new UserAlreadyExistException(String.format("Логин %s уже занят", userToSave.getLogin()));
        }

    }
}