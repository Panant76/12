package by.vitstep.organizer.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Currency;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {
    String name;
    Long amount;
    Currency currency;
}
