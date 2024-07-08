package org.dateroad.feign.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "KakaoFeignApi", url = "https://kapi.kakao.com/v1/user/access_token_info")
public interface KakaoFeignClient {
    @GetMapping
    KakaoAccessTokenInfoRes getKakaoPlatformUserId(
            @RequestHeader("Authorization") String accessTokenWithTokenType);
}
