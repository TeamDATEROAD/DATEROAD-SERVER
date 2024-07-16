package org.dateroad.feign.apple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.UnauthorizedException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class AppleIdentityJWTParser {
    private final ObjectMapper objectMapper;
    private static final int HEADER_INDEX = 0;
    private static final String SPLIYBY = "\\.";

    public Map<String, String> parseHeader(final String identityToken) {
        try {

            //헤더, 페이로드, 서명에서 첫 인덱스인 헤더를 가져옴
            String encodedHeader = identityToken.split(SPLIYBY)[HEADER_INDEX];
            String decodedHeader = new String(Base64.getUrlDecoder().decode(encodedHeader), StandardCharsets.UTF_8);
            return objectMapper.readValue(decodedHeader, Map.class);
        } catch (JsonProcessingException | ArrayIndexOutOfBoundsException e) {
            throw new UnauthorizedException(FailureCode.INVALID_APPLE_IDENTITY_TOKEN);
        }
    }

    public Claims parseWithClientPublicKeyAndGetClaims(final String identityToken, final PublicKey publicKey) {
        try {
            return getJwtParser(publicKey)
                    .parseClaimsJws(identityToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(FailureCode.EXPIRED_APPLE_IDENTITY_TOKEN);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            throw new UnauthorizedException(FailureCode.INVALID_APPLE_IDENTITY_TOKEN);
        }
    }

    private JwtParser getJwtParser(final Key key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
    }
}
