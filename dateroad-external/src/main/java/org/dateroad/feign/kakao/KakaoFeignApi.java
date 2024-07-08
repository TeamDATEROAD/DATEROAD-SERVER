package org.dateroad.feign.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${feign.kakao.name}", url = "${feign.kakao.url}")
public interface KakaoFeignApi {
    @GetMapping
    KakaoAccessTokenInfoRes getKakaoPlatformUserId(@RequestHeader("Authorization") String accessTokenWithTokenType);
}
