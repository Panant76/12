package by.vitstep.organizer.model.entity;

import by.vitstep.organizer.model.entity.enums.TransactionType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import javax.persistence.*;
import java.time.LocalDateTime;
@Data
@Entity
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Enumerated(value = EnumType.STRING)
    TransactionType transactionType;
    @ManyToOne(cascade = CascadeType.REFRESH)
    Account account;
    Float amount;
    LocalDateTime dateTime;
    @ManyToOne
    Friend friend;
}
