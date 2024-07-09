package org.dateroad.feign.apple;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.UnauthorizedException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.PublicKey;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class AppleFeignProvider {
    private final AppleIdentityJWTParser appleIdentityJWTParser;
    private final AppleFeignApi appleFeignApi;
    private final AppleClientPublicKeyGenerator appleClientPublicKeyGenerator;
    private final AppleIdentityJWTValidator appleIdentityJWTValidator;
    private final AppleClientSecretGenerator appleClientSecretGenerator;
    private final AppleProperties appleProperties;

    public String getApplePlatformUserId(final String identityToken) {
        Map<String, String> tokenHeaders = appleIdentityJWTParser.parseHeader(identityToken);
        ApplePublicKeys applePublicKey = appleFeignApi.getApplePublicKeys();
        PublicKey clientPublicKey = appleClientPublicKeyGenerator.generateClientPublicKeyWithApplePublicKeys(tokenHeaders, applePublicKey);
        Claims claims = appleIdentityJWTParser.parseWithClientPublicKeyAndGetClaims(identityToken, clientPublicKey);
        validateClaims(claims);
        return claims.getSubject();
    }

    public void revokeUser(final String authCode) {
        try {
            String secretKey = appleClientSecretGenerator.createClientSecret();
            String appleAccessToken = appleClientSecretGenerator.getAppleAccessToken(secretKey, authCode);
            appleFeignApi.revokeUser(secretKey, appleAccessToken, appleProperties.getClientId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateClaims(final Claims claims) {
        if (!appleIdentityJWTValidator.isValidAppleIdentityToken(claims)) {
            throw new UnauthorizedException(FailureCode.INVALID_APPLE_IDENTITY_TOKEN_CLAIMS);
        }
    }

}
