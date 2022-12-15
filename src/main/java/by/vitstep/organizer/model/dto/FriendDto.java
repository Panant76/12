package by.vitstep.organizer.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendDto {
    Long id;
    String name;
    LocalDate birthday;

}
