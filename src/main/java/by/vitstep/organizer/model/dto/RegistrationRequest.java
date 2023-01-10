package by.vitstep.organizer.model.dto;

import by.vitstep.organizer.model.entity.enums.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationRequest {
    String login;
    String password;
    String name;
    LocalDate birthday;

    @JsonIgnore
    List<Roles> roles;
}
