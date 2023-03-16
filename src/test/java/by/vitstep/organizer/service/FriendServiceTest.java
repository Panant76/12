package by.vitstep.organizer.service;

import by.vitstep.organizer.exception.UserNotFoundException;
import by.vitstep.organizer.model.dto.ContactsDto;
import by.vitstep.organizer.model.dto.FriendDto;
import by.vitstep.organizer.model.entity.Contacts;
import by.vitstep.organizer.model.entity.Friend;
import by.vitstep.organizer.model.entity.User;
import by.vitstep.organizer.model.mapping.FriendMapper;
import by.vitstep.organizer.repository.FriendRepository;
import by.vitstep.organizer.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class FriendServiceTest {
    private AutoCloseable autoCloseable;
    @Mock
    private FriendRepository friendRepository;
    private FriendMapper friendMapper;
    @Mock
    private UserRepository userRepository;
    private FriendService friendService;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        this.friendMapper = Mappers.getMapper(FriendMapper.class);
        this.friendService = new FriendService(friendRepository, friendMapper, userRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    @DisplayName(value = "Должно возникнуть исключение UserNotFoundException")
    void createFriend_exception() {
        UUID uuid = UUID.randomUUID();
        ContactsDto contactsDto = ContactsDto
                .builder()
                .phone("+6964964087")
                .build();
        FriendDto friendDto = FriendDto
                .builder()
                .name("Test")
                .uuid(uuid)
                .contacts(contactsDto)
                .build();
        SecurityContext securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(null, null));
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByPhone(friendDto.getContacts().getPhone()))
                .thenReturn(Optional.of(User.builder().uuid(uuid).build()));

        assertThrows(UserNotFoundException.class,()->friendService.createFriend(friendDto));
    }
    @Test
    @DisplayName(value = "UUID объекта заполняется верно")
    void createFriend_setUuid_ok(){
        UUID uuid = UUID.randomUUID();
        ContactsDto contactsDto = ContactsDto
                .builder()
                .phone("+6964964087")
                .build();
        FriendDto friendDto = FriendDto
                .builder()
                .name("Test")
                .uuid(uuid)
                .contacts(contactsDto)
                .build();
        SecurityContext securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(User.builder().uuid(uuid).build(), null));
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByPhone(friendDto.getContacts().getPhone()))
                .thenReturn(Optional.of(User.builder().uuid(uuid).build()));

        when(friendRepository.save(ArgumentMatchers.argThat(friend ->
                friend.getUuid()!=null && friend.getUuid().equals(uuid)
        && friend.getName().equals("Test")
        && friend.getContacts().getPhone().equals("+6964964087"))))
                .thenReturn(Friend.builder()
                        .name("Test")
                        .uuid(uuid)
                        .contacts(Contacts.builder()
                                .phone("+6964964087")
                                .build())
                        .build());

        FriendDto result=friendService.createFriend(friendDto);

        assertNotNull(result.getUuid());
        assertNotNull(result.getContacts());
        assertEquals(uuid,result.getUuid());
        assertEquals("Test", result.getName());
        assertEquals("+6964964087", result.getContacts().getPhone());

    }

}
