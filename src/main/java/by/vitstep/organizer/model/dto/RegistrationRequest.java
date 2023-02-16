package by.vitstep.organizer.model.dto;

import by.vitstep.organizer.model.entity.enums.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
// Шаблон(Форма) запроса на регистрацию
public class RegistrationRequest {
    @Size(min = 5, max = 12)
    @NotBlank
    String login;
    @Size(min = 8, max = 20)
    @NotBlank
    String password;


    String name;
    @NotBlank
    @Pattern(regexp = "^\\+[1-9]{1}[0-9]{6,16}$")
    //@Pattern(regexp = "^(\\+)+\\d+$")
    //@Size(max = 16, min = 6)
    String phone;
    @Pattern(regexp = "^\\w+@\\w+\\.\\w+$")
    String email;

    LocalDate birthday;

    @JsonIgnore
    List<Roles> roles;
    Boolean createAccounts=false;
}
