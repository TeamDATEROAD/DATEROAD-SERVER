package org.dateroad.auth.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;

@Component
public class KeyProvider {
    private final JwtProperties jwtProperties;

    public KeyProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public Key getSigningKey() {
        return Keys.hmacShaKeyFor(encodeSecretKeyToBase64().getBytes());
    }

    private String encodeSecretKeyToBase64() {
        return Base64.getEncoder().encodeToString(jwtProperties.getSecret().getBytes());
    }
}
