package org.dateroad.feign.apple;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppleIdentityJWTValidator {
    AppleProperties appleProperties;

    public boolean isValidAppleIdentityToken(final Claims claims) {
        return claims.getIssuer().contains(appleProperties.getAud())
                && claims.getAudience().equals(appleProperties.getClientId());
    }
}
