package by.vitstep.organizer.exception;

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
