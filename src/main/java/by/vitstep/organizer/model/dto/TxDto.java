package by.vitstep.organizer.model.dto;

import by.vitstep.organizer.model.entity.enums.TransactionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TxDto {

    Long id;

    TransactionType transactionType;

    Long accountId;

    String accountName;

    Float ammount;

    LocalDateTime dateTime;

    FriendDto friend;
}
