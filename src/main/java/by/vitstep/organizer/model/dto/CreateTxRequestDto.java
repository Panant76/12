package by.vitstep.organizer.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTxRequestDto {
    Long sourceAccountId;
    Long targetAccountId;
    Float amount;
    Boolean isAutoConverted = false;
}
