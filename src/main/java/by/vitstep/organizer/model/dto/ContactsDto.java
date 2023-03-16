package by.vitstep.organizer.model.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.util.List;

@Schema(description = "Описание объекта \"Контакты\"")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactsDto {
    @Schema(title = "Адрес", description = "Адрес", maxLength = 255)
    String address;

    // @Size(min=6,max=16)//при использовании первого патерна, возможно, не требуется
    @Schema(title = "Номер телефона в международном формате", description = "Номер телефона в международном формате", pattern = "^\\(\\+\\d[4]\\)\\d+$")
    @NotBlank
    @Pattern(regexp = "^\\+[1-9]{1}[0-9]{6,16}$")
    //@Pattern(regexp = "^(\\+)+\\d+$")
            String phone;
    @ArraySchema(maxItems = 5, uniqueItems = true, schema = @Schema(title = "Адрес электронной почты", description = "Список адресов электронной почты"))
    @Min(value = 1)
    List<@Email String> email;
    @ArraySchema(maxItems = 10, uniqueItems = true, schema = @Schema(title = "Контакт в мессенджерах", description = "Список контактов в мессенджерах"))

    List<@Pattern(regexp = "^\\@[a-zA-Z0-9._]+$") String> messengers;
}
