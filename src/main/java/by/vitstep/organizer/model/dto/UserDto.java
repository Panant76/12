package by.vitstep.organizer.model.dto;

import by.vitstep.organizer.model.entity.Contacts;
import by.vitstep.organizer.model.entity.Friend;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    Long id;
    String login;

    String name;
    Contacts contacts;
    UUID uuid;
    LocalDate birthday;
    List<Friend> friendList;
}
