package by.vitstep.organizer.repository;

import by.vitstep.organizer.model.entity.Friend;
import by.vitstep.organizer.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FriendRepository extends JpaRepository<Friend,Long> {

    Optional<Friend> findByUuidAndUser(final UUID uuid,final User user);
}
