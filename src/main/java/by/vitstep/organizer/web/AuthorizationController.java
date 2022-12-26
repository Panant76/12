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
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class  AuthorizationController {
    UserService service;
    UserMapper mapper;
    JwtUtil util;
    PasswordEncoder encoder;
    AuthenticationManager manager;

    @PostMapping("/registr")
    public ResponseEntity<UserDto> registr(@RequestBody RegistrationRequest request) {
        request.setPassword(encoder.encode(request.getPassword()));
        request.setRoles(List.of(Roles.USER));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createUser(request));
    }
    @PostMapping("/registrAdmin")
    public ResponseEntity<UserDto> registrAdmin(@RequestBody RegistrationRequest request){
        request.setPassword(encoder.encode(request.getPassword()));
        request.setRoles(List.of(Roles.ADMIN));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createUser(request));
    }
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequest request){
        Authentication authentication=manager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(),request.getPassword()));
        User user=(User)authentication.getPrincipal();
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION,util.generateToken(user))
                .body(mapper.toDto(user));
    }
}
