package org.dateroad.auth.jwt.refreshtoken;

import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.UnauthorizedException;
import org.dateroad.refreshtoken.domain.RefreshToken;
import org.dateroad.refreshtoken.repository.RefreshTokenRepository;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;

@RequiredArgsConstructor
@Component
public class RefreshTokenGenerator {
    private final RefreshTokenRepository refreshTokenRepository;
    private static final int TOKEN_BYTE_SIZE = 60 * 6 / 8; // 45 Bytes

    public String generateRefreshToken(final long userId) {
        SecureRandom random = createSecureRandom();

        //SecureRandom을 사용하여 45 바이트의 랜덤 토큰을 생성
        byte[] token = new byte[TOKEN_BYTE_SIZE];
        random.nextBytes(token);
        LocalDateTime expireAt = LocalDateTime.now().plusWeeks(2);

        RefreshToken newRefreshToken = RefreshToken.create(token, userId, expireAt);
        refreshTokenRepository.save(newRefreshToken);

        return Base64.getEncoder().encodeToString(token);
    }

    //refreshToken 재발급할 때 검증
    public Long getUserIdOrThrow(final String refreshToken) {
        byte[] convertedRefreshToken = toBinary(refreshToken);

        RefreshToken findRefreshToken = refreshTokenRepository.findUserIdByToken(Arrays.toString(convertedRefreshToken).getBytes());
        if (findRefreshToken == null) {
            throw new UnauthorizedException(FailureCode.INVALID_REFRESH_TOKEN_VALUE);
        }

        if (findRefreshToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedException(FailureCode.EXPIRED_REFRESH_TOKEN);
        }
        return findRefreshToken.getUserId();
    }

    public void deleteRefreshToken(final Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    private SecureRandom createSecureRandom() {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);

        //현재 시간을 바이트 배열로 변환하여 SecureRandom의 초기값(seed)으로 사용
        buffer.putLong(System.currentTimeMillis());
        return new SecureRandom(buffer.array());
    }

    //Base64 인코딩된 리프레시 토큰 문자열을 바이트 배열
    private byte[] toBinary(String refreshToken) {
        return Base64.getDecoder().decode(refreshToken);
    }
}
