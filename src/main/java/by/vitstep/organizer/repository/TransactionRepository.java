package by.vitstep.organizer.repository;

import by.vitstep.organizer.model.entity.Account;
import by.vitstep.organizer.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("select * from Transaction where (sourceAccount = :account or targetAccount =:account) and dateTime<:before")
    List<Transaction> findByAccount(final Account account, final LocalDateTime before);
}
