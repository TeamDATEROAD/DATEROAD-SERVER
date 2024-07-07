package org.dateroad.feign.apple;

import org.dateroad.code.FailureCode;
import org.dateroad.exception.UnauthorizedException;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@Component
public class AppleClientPublicKeyGenerator {
    private static final String ALG = "alg";
    private static final String KID = "kid";

    public PublicKey generateClientPublicKeyWithApplePublicKeys(final Map<String, String> headers, final ApplePublicKeys applePublicKeys) {
        ApplePublicKey applePublicKey = applePublicKeys
                .getMatchesKey(headers.get(ALG), headers.get(KID));

        byte[] nBytes = Base64.getUrlDecoder().decode(applePublicKey.n());
        byte[] eBytes = Base64.getUrlDecoder().decode(applePublicKey.e());

        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(
                new BigInteger(1, nBytes), new BigInteger(1, eBytes));

        try {
            KeyFactory keyFactory = KeyFactory.getInstance(applePublicKey.kty());
            return keyFactory.generatePublic(rsaPublicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            throw new UnauthorizedException(FailureCode.UNABLE_MAKE_APPLE_PUBLIC_KEY);
        }
    }
}
