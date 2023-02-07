package by.vitstep.organizer.repository;

import by.vitstep.organizer.model.entity.Account;
import by.vitstep.organizer.model.entity.Archive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long> {
    Optional <Archive> findByAccount(Account account);
    @Query(value = "select * from archive order by id desc limit 1", nativeQuery = true)
    Optional<Archive> findLast();
}
