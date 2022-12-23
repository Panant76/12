package by.vitstep.organizer.web;

import by.vitstep.organizer.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {
    UserService userService;

//    @PostMapping("/create")
//    public ResponseEntity<UserDto> createUser(@RequestBody UserDto user) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
//    }
//
//    @GetMapping("/get")
//    public ResponseEntity<UserDto> getUser(@RequestParam Long id) {
//        return ResponseEntity.ok(userService.getUser(id));
//
//    }
}