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
    @Query("select tx from Transaction tx where (tx.sourceAccount = :account or tx.targetAccount =:account) and tx.dateTime<:before")
    List<Transaction> findByAccount(final Account account, final LocalDateTime before);
}
