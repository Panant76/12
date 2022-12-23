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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
