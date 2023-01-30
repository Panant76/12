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
public class RegistrationRequest {
    @Size(min = 5, max = 15)
    @NotBlank
    String login;
    @Size(min = 8, max = 20)
    @NotBlank
    String password;
    @Pattern(regexp = "^\\w+@\\w+\\.\\w+$")
    String email;
    String name;
    LocalDate birthday;

    @JsonIgnore
    List<Roles> roles;
}
