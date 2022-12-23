package by.vitstep.organizer.utils;

import by.vitstep.organizer.model.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityUtil {

    public static Optional<User> getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .map(User.class::cast);
    }
}
