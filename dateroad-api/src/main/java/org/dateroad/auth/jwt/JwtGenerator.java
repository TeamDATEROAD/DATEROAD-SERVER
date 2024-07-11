package org.dateroad.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.UnauthorizedException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtGenerator {
    private final JwtProperties jwtProperties;

    public String generateAccessToken(final long userId) {
        final Date now = new Date();
        final Date expireDate = generateExpirationDate(now);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Date generateExpirationDate(final Date now) {
        return new Date(now.getTime() + jwtProperties.getAccessTokenExpireTime());
    }

    public Key getSigningKey() {
        return Keys.hmacShaKeyFor(encodeSecretKeyToBase64().getBytes());
    }

    private String encodeSecretKeyToBase64() {
        return Base64.getEncoder().encodeToString(jwtProperties.getSecret().getBytes());
    }

    public Jws<Claims> parseToken(String token) {
        try {
            JwtParser jwtParser = getJwtParser();
            return jwtParser.parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(FailureCode.EXPIRED_ACCESS_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new UnauthorizedException(FailureCode.UNSUPPORTED_TOKEN_TYPE);
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException(FailureCode.MALFORMED_TOKEN);
        } catch (SignatureException e) {
            throw new UnauthorizedException(FailureCode.INVALID_SIGNATURE_TOKEN);
        } catch (Exception e) {
            throw new UnauthorizedException(FailureCode.INVALID_ACCESS_TOKEN_VALUE);
        }
    }

    public JwtParser getJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build();
    }
}
