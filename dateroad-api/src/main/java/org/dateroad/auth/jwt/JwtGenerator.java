package org.dateroad.auth.jwt;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtGenerator {
    private final JwtProperties jwtProperties;

    public String generateToken(Long userId, TokenType tokenType) {
        final Date now = new Date();
        final Date expiryDate = generateExpirationDate(now, tokenType);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Key getSigningKey() {
        return Keys.hmacShaKeyFor(encodeSecretKeyToBase64().getBytes());
    }

    private Date generateExpirationDate(Date now, TokenType tokenType) {
        if ( tokenType == TokenType.ACCESS_TOKEN) {
            return new Date(now.getTime() + jwtProperties.getAccessTokenExpireTime());
        } else {
            return new Date(now.getTime() + jwtProperties.getRefreshTokenExpireTime());
        }
    }

    //jwtSecretKey를 Base64로 인코드하는 메서드
    @Bean
    private String encodeSecretKeyToBase64() {
        return Base64.getEncoder().encodeToString(jwtProperties.getSecret().getBytes());
    }
}
