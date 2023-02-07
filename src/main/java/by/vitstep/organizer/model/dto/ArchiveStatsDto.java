package by.vitstep.organizer.model.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
@Data
@SuperBuilder
public  class ArchiveStatsDto {
    String accountName;
    LocalDate dateFrom;
    LocalDate dateTo;

}
