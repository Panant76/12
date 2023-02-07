package by.vitstep.organizer.web;

import by.vitstep.organizer.model.dto.LoginRequest;
import by.vitstep.organizer.model.dto.RegistrationRequest;
import by.vitstep.organizer.model.dto.UserDto;
import by.vitstep.organizer.model.entity.User;
import by.vitstep.organizer.model.entity.enums.Roles;
import by.vitstep.organizer.model.mapping.UserMapper;
import by.vitstep.organizer.security.JwtUtil;
import by.vitstep.organizer.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/auth")

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorizationController {
    UserService service;
    UserMapper mapper;
    JwtUtil util;
    PasswordEncoder encoder;
    AuthenticationManager manager;

    public AuthorizationController(UserService service,
                                   UserMapper mapper,
                                   JwtUtil util,
                                   @Qualifier("major") PasswordEncoder encoder,
                                   AuthenticationManager manager) {
        this.service = service;
        this.mapper = mapper;
        this.util = util;
        this.encoder = encoder;
        this.manager = manager;
    }

    @PostMapping("/registr")
    public ResponseEntity<UserDto> registr(@RequestBody @Valid RegistrationRequest request) {
        request.setPassword(encoder.encode(request.getPassword()));
        request.setRoles(List.of(Roles.ROLE_USER));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createUser(request));
    }

    //@RolesAllowed("ROLE_ADMIN")
    @PostMapping("/registrAdmin")
    public ResponseEntity<UserDto> registrAdmin(@RequestBody RegistrationRequest request) {
        request.setPassword(encoder.encode(request.getPassword()));
        request.setRoles(List.of(Roles.ROLE_ADMIN));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequest request) {
        Authentication authentication = manager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, util.generateToken(user))
                .body(mapper.toDto(user));
    }
}
