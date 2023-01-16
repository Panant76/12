package by.vitstep.organizer.service;

import by.vitstep.organizer.model.dto.ContactsDto;
import by.vitstep.organizer.model.entity.Contacts;
import by.vitstep.organizer.model.entity.Friend;
import by.vitstep.organizer.model.entity.User;
import by.vitstep.organizer.model.mapping.ContactsMapper;
import by.vitstep.organizer.repository.FriendRepository;
import by.vitstep.organizer.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ContactService {
    UserRepository userRepository;
    FriendRepository friendRepository;
    ContactsMapper contactsMapper;

    public Long createUserContact(Long userId, ContactsDto contacts) {
        return userRepository
                .findById(userId)
                .map(user -> {
                    user.setContacts(contactsMapper.toEntity(contacts));
                    return userRepository.save(user);
                })

                .map(user -> user.getContacts().getId())
                .orElse(null);
    }

    public Contacts createFriendContact(Long friendId, Contacts contacts) {
        return friendRepository
                .findById(friendId)
                .map(friend -> {
                    friend.setContacts(contacts);
                    return friendRepository.save(friend);
                })
                .map(Friend::getContacts)
                .orElse(null);

    }

    public Contacts getUserContacts(Long userId, Contacts contacts) {
        return null;
    }
}
