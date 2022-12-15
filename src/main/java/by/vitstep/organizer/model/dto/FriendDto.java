package by.vitstep.organizer.model.dto;

import by.vitstep.organizer.model.entity.Contacts;
import by.vitstep.organizer.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendDto {
    Long id;
    String name;
    Contacts contacts;
    LocalDate birthday;
    User user;
}
