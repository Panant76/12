package by.vitstep.organizer.model.dto.ananlytics;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class FriendShortInfoDto {
    Long id;
    String name;
}
