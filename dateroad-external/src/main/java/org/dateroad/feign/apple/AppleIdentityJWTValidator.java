package org.dateroad.feign.apple;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppleIdentityJWTValidator {
    @Value("${feign.apple.iss}")
    private String iss;
    @Value("${feign.apple.client-id}")
    private String clientId;

    public boolean isValidAppleIdentityToken(final Claims claims) {
        return claims.getIssuer().contains(iss)
                && claims.getAudience().equals(clientId);
    }
}
