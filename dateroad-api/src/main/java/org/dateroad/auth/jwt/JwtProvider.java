package org.dateroad.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtProvider {
    private final JwtGenerator jwtGenerator;

    public Token issueToken(final long userId) {
        return Token.of(
                jwtGenerator.generateToken(userId, TokenType.ACCESS_TOKEN),
                jwtGenerator.generateToken(userId, TokenType.REFRESH_TOKEN)
        );
    }

    public long getUserIdFromSubject(String token) {
        Jws<Claims> jws = jwtGenerator.parseToken(token);
        String subject = jws.getBody().getSubject();

        //subject가 숫자문자열인지 예외처리
        try {
            return Long.parseLong(subject);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.valueOf(FailureCode.TOKEN_SUBJECT_NOT_NUMERIC_STRING));
        }
    }
}
