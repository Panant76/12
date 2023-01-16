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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column(unique = true)
    String name;
    Float amount;
    @Enumerated(value = EnumType.STRING)
    Currency currency;

    @ManyToOne
    User user;
}