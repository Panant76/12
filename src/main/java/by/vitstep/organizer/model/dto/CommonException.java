package by.vitstep.organizer.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
@AllArgsConstructor
@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommonException {
    Integer code;
    String message;
}
