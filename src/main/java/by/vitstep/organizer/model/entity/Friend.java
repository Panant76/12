package by.vitstep.organizer.model.entity;


import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Friend {
    private static final String SEQ_NAME = "friend_id_seq";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    Long id;
    UUID uuid;
    String name;
    @OneToOne(cascade = CascadeType.ALL,optional = false)
    Contacts contacts;
    LocalDate birthday;
    @ManyToOne
    User user;
}
