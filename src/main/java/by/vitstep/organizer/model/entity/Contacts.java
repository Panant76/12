package by.vitstep.organizer.model.entity;


import lombok.*;
import lombok.experimental.FieldDefaults;
import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Contacts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String address;
    @ElementCollection
    List<String> phone;
    @ElementCollection
    List<String> email;
    @ElementCollection
    List<String> messengers;

}
