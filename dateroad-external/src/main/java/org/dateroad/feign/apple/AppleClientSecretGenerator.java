package org.dateroad.feign.apple;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.UnauthorizedException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class AppleClientSecretGenerator {
    private final AppleProperties appleProperties;

    public String createClientSecret() throws IOException {
        Date expirationDate = Date.from(LocalDateTime.now().plusDays(29).atZone(ZoneId.systemDefault()).toInstant());
        Map<String, Object> jwtHeader = new HashMap<>();
        jwtHeader.put("kid", appleProperties.getKid()); // kid
        jwtHeader.put("alg", appleProperties.getAlg()); // alg
        return Jwts.builder()
                .setHeaderParams(jwtHeader)
                .setIssuer(appleProperties.getIss()) // issuer
                .setIssuedAt(new Date(System.currentTimeMillis())) // 발행 시간
                .setExpiration(expirationDate) // 만료 시간
                .setAudience(appleProperties.getAud()) // aud
                .setSubject(appleProperties.getClientId()) // sub
                .signWith(SignatureAlgorithm.ES256, getPrivateKey())
                .compact();
    }

    public PrivateKey getPrivateKey() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/AuthKey_39CUV6ST46.p8"); // .p8 key파일 위치
        String privateKey = new String(Files.readAllBytes(Paths.get(resource.getURI())));

        Reader pemReader = new StringReader(privateKey);
        PEMParser pemParser = new PEMParser(pemReader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
        return converter.getPrivateKey(object);
    }
}
