package by.vitstep.organizer.model.entity;

import by.vitstep.organizer.model.entity.enums.Currency;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    private static final String SEQ_NAME = "account_id_seq";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    Long id;
    @Column(unique = true)
    String name;
    Float amount;
    @Enumerated(value = EnumType.STRING)
    Currency currency;

    @ManyToOne
    User user;
}