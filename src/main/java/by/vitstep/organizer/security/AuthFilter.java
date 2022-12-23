package by.vitstep.organizer.security;


import by.vitstep.organizer.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;



@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {
    JwtUtil jwtUtil;
    UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(authHeader) || !authHeader.contains("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String token = authHeader.split(" ")[1];
        final Claims claims = jwtUtil.validateAndGet(token);
        Long id = Long.parseLong((String) claims.get("userId"));
        if (id == null) {
            filterChain.doFilter(request, response);
            return;
        }
        userService.getById(id)
                .ifPresentOrElse(user -> SecurityContextHolder
                                .getContext()
                                .setAuthentication(new UsernamePasswordAuthenticationToken(user, null, CollectionUtils.isEmpty(user.getAuthorities()) ? List.of() :
                                        user.getAuthorities())),
                        () -> SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(null, null, List.of())));
        filterChain.doFilter(request, response);

    }
}

