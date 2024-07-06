package org.dateroad.auth.jwt;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtGenerator {
    private final JwtProperties jwtProperties;
    private final KeyProvider keyProvider;

    public String generateToken(final long userId, final TokenType tokenType) {
        final Date now = new Date();
        final Date expiryDate = generateExpirationDate(now, tokenType);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(keyProvider.getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Date generateExpirationDate(Date now, TokenType tokenType) {
        if ( tokenType == TokenType.ACCESS_TOKEN) {
            return new Date(now.getTime() + jwtProperties.getAccessTokenExpireTime());
        } else if ( tokenType == TokenType.REFRESH_TOKEN){
            return new Date(now.getTime() + jwtProperties.getRefreshTokenExpireTime());
        } else {
            throw new EntityNotFoundException(FailureCode.TOKEN_TYPE_NOT_FOUND);
        }
    }
}
