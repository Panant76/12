package by.vitstep.organizer.model.entity;


import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    UUID uuid;
    String name;
    @OneToOne
    Contacts contacts;
    LocalDate birthday;
    @ManyToOne
    User user;
}
