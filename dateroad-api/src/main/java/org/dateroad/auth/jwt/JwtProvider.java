package org.dateroad.auth.jwt;

import io.jsonwebtoken.JwtParser;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
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

    public Long getSubject(String token) {
        JwtParser jwtParser = jwtValidator.getJwtParser();
        return Long.valueOf(jwtParser.parseClaimsJws(token)
                .getBody()
                .getSubject());
    }
}
