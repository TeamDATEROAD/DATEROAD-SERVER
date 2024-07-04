package org.dateroad.auth.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.common.FailureResponse;
import org.dateroad.exception.UnauthorizedException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class JwtValidator {

    private final JwtGenerator jwtGenerator;


    public void validateAccessToken(String accessToken) {
        try {
            parseToken(accessToken);
        } catch (Exception e) {
            throw new UnauthorizedException(FailureCode.UNAUTHORIZED);
        }
    }

    public void validateRefreshToken(String refreshToken) {
        try {
            parseToken(refreshToken);
        } catch (Exception e) {
            throw new UnauthorizedException(FailureCode.UNAUTHORIZED);
        }
    }

    public void equalRefreshToken(String refreshToken, String storedRefreshToken) {
        if (!refreshToken.equals(storedRefreshToken)) {
            throw new UnauthorizedException(FailureCode.UNAUTHORIZED);
        }
    }

    private void parseToken(String token) {
        JwtParser parser = getJwtParser();
        parser.parseClaimsJws(token);
    }

    public JwtParser getJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(jwtGenerator.getSigningKey())
                .build();
    }
}
