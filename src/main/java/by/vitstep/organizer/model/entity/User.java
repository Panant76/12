package by.vitstep.organizer.model.entity;


import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "org_user")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column(unique = true)
    String login;
    String password;
    String name;
    @OneToOne(cascade = CascadeType.ALL)
    Contacts contacts;
    LocalDate birthday;
    @ManyToMany(mappedBy = "user")
    List<Friend> friendList;

}
