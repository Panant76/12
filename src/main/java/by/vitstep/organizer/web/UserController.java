package by.vitstep.organizer.web;

import by.vitstep.organizer.model.dto.FriendDto;
import by.vitstep.organizer.service.FriendService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {
    FriendService friendService;

    @PutMapping("/putFriend")
    public ResponseEntity<FriendDto> createFriend(@RequestBody @Valid FriendDto friendDto) {
        return ResponseEntity.ok(friendService.createFriend(friendDto));
    }
}