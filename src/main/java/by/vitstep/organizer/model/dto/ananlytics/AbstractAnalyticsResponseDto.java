package by.vitstep.organizer.model.dto.ananlytics;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class AbstractAnalyticsResponseDto {
    String accountName;
    LocalDateTime dateFrom;
    LocalDateTime dateTo;
    FriendShortInfoDto friend;
}
