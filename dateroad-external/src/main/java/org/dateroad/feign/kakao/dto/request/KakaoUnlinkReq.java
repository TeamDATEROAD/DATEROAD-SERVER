package org.dateroad.feign.kakao.dto.request;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record KakaoUnlinkReq(
        String target_id_type,
        Long target_id
) {
    public static KakaoUnlinkReq of(Long target_id) {
        return KakaoUnlinkReq
                .builder()
                .target_id_type("user_id")
                .target_id(target_id)
                .build();
    }
}
