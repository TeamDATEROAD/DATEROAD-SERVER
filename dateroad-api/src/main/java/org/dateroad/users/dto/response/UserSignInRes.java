package org.dateroad.users.dto.response;

import lombok.AccessLevel;
import lombok.Builder;


@Builder(access = AccessLevel.PRIVATE)
public record UserSignInRes(
        Long userId,
        String accessToken,
        String refreshToken
) {
    public static UserSignInRes of(final long userId, final String accessToken, final String refreshToken) {
        return UserSignInRes.builder()
                .userId(userId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
