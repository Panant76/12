package by.vitstep.organizer.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendDto {
    @NotBlank
    String name;
    LocalDate birthday;
    @NotNull
    ContactsDto contacts;
    UUID uuid;
}
