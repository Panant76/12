package by.vitstep.organizer.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Представление объекта \"Друг\"")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendDto {
    @Schema(title = "Имя", description = "Имя", maxLength = 50, example = "ООО \"Добро и палки\"")
    @NotBlank
    String name;

    @Schema(title = "День рождения", description = "День рождения", format = "date")
    LocalDate birthday;

    @NotNull
    ContactsDto contacts;

    @Schema(title = "Сквозной id по связи user-friend", description = "Сквозной id по связи user-friend")
    UUID uuid;
}
