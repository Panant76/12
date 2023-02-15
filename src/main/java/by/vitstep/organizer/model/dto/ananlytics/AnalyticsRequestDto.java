package by.vitstep.organizer.model.dto.ananlytics;

import by.vitstep.organizer.model.dto.enums.ArchiveStatsType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AnalyticsRequestDto {
    Long accountId;
    LocalDateTime dateFrom;
    LocalDateTime dateTo;
    ArchiveStatsType type;
    Float greaterThan;
    Float lessThan;
    List<Long> friendsIdList;
}
