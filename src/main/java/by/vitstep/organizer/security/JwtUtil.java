package by.vitstep.organizer.security;

import by.vitstep.organizer.config.ProjectConfiguration;
import by.vitstep.organizer.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtUtil {
    ProjectConfiguration config;

    /**
     * Содержание токена:
     * 1. subject: organizer
     * 2. userId: userId
     * 3. expiration time: дата-время (для теста можно установить в 2 минуты)
     * 4. секретный ключ
     * 5. алгоритм шифрования
     * Ex.:{
     * header: {
     * alg: hmac,
     * typ: JWT
     * },
     * payload: {
     * sub: organizer,
     * userId: ${user.id},
     * expiration: ${екущее время + 2 минуты}
     * issued: expiration - 2 minutes
     * },
     * signature: {
     * binaryData
     * }
     * }
     *
     * @param user - пользователь
     * @return строку - JSON-токен
     */
    public String generateToken(final User user) {
        final SecretKey key = getSecretKey();
        final Date expirationDate = Date.from(LocalDateTime.now()
                .plusHours(config.getSecurity().getTokenLifetimeHours())
                .atZone(ZoneId.systemDefault())
                .toInstant());
        return Jwts.builder()
                .setSubject("organizer")
                .addClaims(Map.of("userId", user.getId().toString()))
                .setExpiration(expirationDate)
                .setIssuedAt(new Date())
                .signWith(key)
                .compact();

    }
    public Claims validateAndGet(final String token){
        final SecretKey key=getSecretKey();
        return Jwts.parserBuilder()
                .requireSubject("organizer")
                .setSigningKey(key)
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(config.getSecurity().getSecretKey().getBytes(StandardCharsets.UTF_8));
    }
}
