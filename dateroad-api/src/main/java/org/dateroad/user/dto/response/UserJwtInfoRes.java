package org.dateroad.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserJwtInfoRes(
        Long userId,
        String accessToken,
        String refreshToken
) {
    public static UserJwtInfoRes of(final Long userId, final String accessToken, final String refreshToken) {
        return UserJwtInfoRes.builder()
                .userId(userId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
