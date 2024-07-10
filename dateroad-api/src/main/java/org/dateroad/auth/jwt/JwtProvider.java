package org.dateroad.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.dateroad.auth.jwt.refreshtoken.RefreshTokenGenerator;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.UnauthorizedException;
import org.dateroad.refreshtoken.domain.RefreshToken;
import org.dateroad.refreshtoken.repository.RefreshTokenRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;

@RequiredArgsConstructor
@Component
public class JwtProvider {
    private final JwtGenerator jwtGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final RefreshTokenRepository refreshTokenRepository;

    public Token issueToken(final long userId) {
        return Token.of(
                jwtGenerator.generateAccessToken(userId),
                refreshTokenGenerator.generateRefreshToken(userId)
        );
    }


    //refreshToken 재발급할 때 검증
    public void validateRefreshToken(LocalDateTime expireDate) {
        if (expireDate.isBefore(LocalDateTime.now())) {
            throw new UnauthorizedException(FailureCode.EXPIRED_REFRESH_TOKEN);
        }
    }


    //Base64 인코딩된 리프레시 토큰 문자열을 바이트 배열
    private byte[] toBinary(String refreshToken) {
        return Base64.getDecoder().decode(refreshToken);
    }

    public long getUserIdFromSubject(String token) {
        Jws<Claims> jws = jwtGenerator.parseToken(token);
        String subject = jws.getBody().getSubject();

        //subject가 숫자문자열인지 예외처리
        try {
            return Long.parseLong(subject);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.valueOf(FailureCode.TOKEN_SUBJECT_NOT_NUMERIC_STRING));
        }
    }
}
