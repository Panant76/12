package by.vitstep.organizer.model.dto;

import by.vitstep.organizer.model.entity.enums.Currency;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillDto {
    String accountName;
    @NotNull
    Long id;
    @NotNull Currency currency;
    LocalDateTime transactionDate;
    @NotNull
    Float amount;

}
