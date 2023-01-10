package by.vitstep.organizer.model.dto;

import by.vitstep.organizer.model.entity.enums.Currency;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTxRequestDto {
    Long sourceAccountId;
    Long targetAccountId;
    Float amount;
    Boolean isAutoConverted = false;
}
