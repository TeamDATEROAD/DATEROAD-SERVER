package org.dateroad.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.common.FailureResponse;
import org.dateroad.exception.UnauthorizedException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtValidator {
    private final JwtGenerator jwtGenerator;

    public void equalRefreshToken(String refreshToken, String storedRefreshToken) {
        if (!refreshToken.equals(storedRefreshToken)) {
            throw new UnauthorizedException(FailureCode.UNAUTHORIZED);
        }
    }

    public Jws<Claims> parseToken(String token) {
        try {
            JwtParser jwtParser = getJwtParser();
            return jwtParser.parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(FailureCode.EXPIRED_ACCESS_TOKEN);
        } catch (Exception e) {
            throw new UnauthorizedException(FailureCode.INVALID_ACCESS_TOKEN_VALUE);
        }
    }

    public JwtParser getJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(jwtGenerator.getSigningKey())
                .build();
    }
}
