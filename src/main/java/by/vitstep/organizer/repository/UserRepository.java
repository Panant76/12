package by.vitstep.organizer.repository;

import by.vitstep.organizer.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(final String login);

    @Query("select u from User u where u.contacts.id in(select id from Contacts where phone=:phone)")
    Optional<User> findByPhone(final String phone);
}
