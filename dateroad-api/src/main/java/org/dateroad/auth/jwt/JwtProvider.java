package org.dateroad.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    private final JwtGenerator jwtGenerator;
    private final JwtValidator jwtValidator;

    public Token issueToken(Long userId) {
        return Token.of(
                jwtGenerator.generateToken(userId, TokenType.ACCESS_TOKEN),
                jwtGenerator.generateToken(userId, TokenType.REFRESH_TOKEN)
        );
    }

    public Long getUserIdFromSubject(String token) {
        Jws<Claims> jws = jwtValidator.parseToken(token);
        return Long.valueOf(jws
                .getBody()
                .getSubject());
    }
}
