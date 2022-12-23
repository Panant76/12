package by.vitstep.organizer.repository;

import by.vitstep.organizer.model.entity.Account;
import by.vitstep.organizer.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByIdAndUser(Long id, User user);
}
