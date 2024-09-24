package org.dateroad.feign.kakao.dto.response;

public record KakaoAccessTokenInfoRes(
        Long id,
        Integer expiresInMillis,
        Integer expires_in,
        Integer app_id,
        Integer appId
) {
}
