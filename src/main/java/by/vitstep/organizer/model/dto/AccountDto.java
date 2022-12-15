package by.vitstep.organizer.model.dto;

import by.vitstep.organizer.model.entity.enums.Currency;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
