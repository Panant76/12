package by.vitstep.organizer.model.dto;

import by.vitstep.organizer.model.entity.Contacts;
import by.vitstep.organizer.model.entity.Friend;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    //Long id;
    String login;
    String password;
    String name;
    Contacts contacts;
    LocalDate birthday;
    List<Friend> friendList;
}
