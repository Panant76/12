package by.vitstep.organizer.web;

import by.vitstep.organizer.model.entity.Contacts;
import by.vitstep.organizer.service.ContactService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ContactController {
    ContactService contactService;
@PostMapping("/createUserContact")
    public ResponseEntity<Contacts> createUserContact(@RequestParam Long userId, @RequestBody Contacts contacts) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(contactService.createUserContact(userId,contacts));
    }
    @PostMapping("/createFriendContact")
    public ResponseEntity<Contacts> createFriendContact(@RequestParam Long friendId, @RequestBody Contacts contacts) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(contactService.createFriendContact(friendId,contacts));
    }
}
