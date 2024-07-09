package org.dateroad.feign.apple;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${feign.apple.name}", url = "${feign.apple.url}")
public interface AppleFeignApi {

    @GetMapping("/keys")
    ApplePublicKeys getApplePublicKeys();

    @PostMapping("/token")
    AppleTokenRes getAppleToken(@RequestParam("client_secret") String clientSecret,
                         @RequestParam("code") String authCode,
                         @RequestParam("grant_type") String grantType,
                         @RequestParam("client_id") String clientId);

    @PostMapping("/revoke")
    void revokeUser(@RequestParam("client_secret") String clientSecret,
                    @RequestParam("accessToken") String accessToken,
                    @RequestParam("client_id") String clientId);
}
