package by.vitstep.organizer.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AllArchiveStatsDto extends ArchiveStatsDto {
    Float income;
    Float spend;
}
