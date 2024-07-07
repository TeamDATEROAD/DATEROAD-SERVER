package org.dateroad.feign.apple;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.UnauthorizedException;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class ApplePlatformUserIdProvider {
    private final AppleIdentityJWTParser appleIdentityJWTParser;
    private final AppleFeignApi appleFeignApi;
    private final AppleClientPublicKeyGenerator appleClientPublicKeyGenerator;
    private final AppleIdentityJWTValidator appleIdentityJWTValidator;

    public String getKakaoPlatformUserId(final String identityToken) {
        Map<String, String> tokenHeaders = appleIdentityJWTParser.parseHeader(identityToken);
        ApplePublicKeys applePublicKey = appleFeignApi.getApplePublicKeys();
        PublicKey clientPublicKey = appleClientPublicKeyGenerator.generateClientPublicKeyWithApplePublicKeys(tokenHeaders, applePublicKey);
        Claims claims = appleIdentityJWTParser.parseWithClientPublicKeyAndGetClaims(identityToken, clientPublicKey);
        validateClaims(claims);
        return claims.getSubject();
    }

    private void validateClaims(final Claims claims) {
        if (!appleIdentityJWTValidator.isValidAppleIdentityToken(claims)) {
            throw new UnauthorizedException(FailureCode.INVALID_APPLE_IDENTITY_TOKEN_CLAIMS);
        }
    }
}
