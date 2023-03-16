package by.vitstep.organizer.repository;

import by.vitstep.organizer.model.entity.Account;
import by.vitstep.organizer.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByIdAndUser(Long id, User user);
    @Query(value = "select currval(account_id_seq) from account", nativeQuery = true)
    Long findCurrentSeq();
}
