package org.dateroad.feign.kakao;

import org.dateroad.feign.kakao.dto.response.KaKaoUnlinkRes;
import org.dateroad.feign.kakao.dto.response.KakaoAccessTokenInfoRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "${feign.kakao.name}", url = "${feign.kakao.url}")
public interface KakaoFeignApi {
    @GetMapping("/access_token_info")
    KakaoAccessTokenInfoRes getKakaoPlatformUserId(@RequestHeader("Authorization") String accessTokenWithTokenType);

    @PostMapping("/unlink")
    KaKaoUnlinkRes unlink(@RequestHeader("Authorization") String appAdminKey,
                          @RequestParam("target_id_type") String targetIdType,
                          @RequestParam("target_id") Long tagerId);
}
