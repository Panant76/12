package by.vitstep.organizer.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Archive {
    private static final String SEQ_NAME = "archive_id_seq";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    Long Id;
    @ManyToOne(fetch = FetchType.LAZY)
    Account account;
    Float incom;
    Float spend;
    LocalDate dateFrom;
    LocalDate dateTo;
}
