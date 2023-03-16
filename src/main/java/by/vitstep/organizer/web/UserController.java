package by.vitstep.organizer.web;

import by.vitstep.organizer.model.dto.FriendDto;
import by.vitstep.organizer.service.FriendGroupService;
import by.vitstep.organizer.service.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
@Tag(name = "Операции пользователя", description = "API операций с пользователем")
@RestController
@RequestMapping("/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {
    FriendService friendService;
    FriendGroupService friendGroupService;

    @Operation(description = "Создать объект \"ДРУГ\" для текущего  пользователя",
    responses={
            @ApiResponse(responseCode = "404",description = "Текущий пользователь не авторизован или удален из базы данных"),
        @ApiResponse(responseCode = "200", description = "Объект \"Друг\" успешно добавлен для текущего пользователя")
    })

    @PutMapping("/putFriend")
    public ResponseEntity<FriendDto> createFriend(@RequestBody @Valid FriendDto friendDto) {
        return ResponseEntity.ok(friendService.createFriend(friendDto));
    }

    @PostMapping("/createGroup")
    public ResponseEntity<Long> createFriendGroup(@RequestParam String name){
        return ResponseEntity.ok(friendGroupService.createFriendGroup(name));
    }
    @PutMapping("/putFriendsToGroup")
    public ResponseEntity<String> putFriendsToGroup(@RequestBody List<Long> friendIds,@RequestParam Long groupId){
        return ResponseEntity.ok(friendGroupService.putFriendsToGroup(friendIds,groupId));
    }
}