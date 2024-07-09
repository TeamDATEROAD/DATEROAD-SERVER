package org.dateroad.feign.kakao;

import org.dateroad.feign.kakao.dto.request.KakaoUnlinkReq;
import org.dateroad.feign.kakao.dto.response.KaKaoUnlinkRes;
import org.dateroad.feign.kakao.dto.response.KakaoAccessTokenInfoRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${feign.kakao.name}", url = "${feign.kakao.url}")
public interface KakaoFeignApi {
    @GetMapping("/access_token_info")
    KakaoAccessTokenInfoRes getKakaoPlatformUserId(@RequestHeader("Authorization") String accessTokenWithTokenType);

    @PostMapping("/unlink")
    void unlink(@RequestHeader("Authorization") String appAdminKey,
                          @RequestBody KakaoUnlinkReq kakaoUnlinkReq);

}
