package org.dateroad.feign.apple;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppleIdentityJWTValidator {
    private final AppleProperties appleProperties;

    public boolean isValidAppleIdentityToken(final Claims claims) {
        return claims.getIssuer().contains(appleProperties.getAud())
                && claims.getAudience().equals(appleProperties.getClientId());
    }
}
