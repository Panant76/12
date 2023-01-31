package by.vitstep.organizer.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactsDto {

    String address;
   // @Size(min=6,max=16)//при использовании первого патерна, возможно, не требуется
    @NotBlank
    @Pattern(regexp = "^\\+[1-9]{1}[0-9]{6,16}$")
    //@Pattern(regexp = "^(\\+)+\\d+$")
    String phone;
    @Min(value = 1)
    List<@Email String> email;

    List<@Pattern(regexp = "^\\@[a-zA-Z0-9._]+$") String> messengers;
}
