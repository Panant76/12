package by.vitstep.organizer.model.dto.ananlytics;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class MultipleTypesAnalyticsResponseDto extends AbstractAnalyticsResponseDto{
    Float incomeAmount;
    Float spendAmount;
}
