package org.dateroad.users.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UsersignUpRes(
        Long userId,
        String accessToken,
        String refreshToken
) {
    public static UsersignUpRes of(final Long userId, final String accessToken, final String refreshToken) {
        return UsersignUpRes.builder()
                .userId(userId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
