package by.vitstep.organizer.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction {
    private static final String SEQ_NAME = "transaction_id_seq";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    Long id;
    @JoinColumn(name = "source_account")
    @ManyToOne(cascade = CascadeType.REFRESH)
    Account sourceAccount;
    @JoinColumn(name = "target_account")
    @ManyToOne(cascade = CascadeType.REFRESH)
    Account targetAccount;
    Float amount;
    LocalDateTime dateTime;
    @ManyToOne
    Friend friend;
}
