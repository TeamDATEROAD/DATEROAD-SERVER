package org.dateroad.feign.apple;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${feign.apple.name}", url = "${feign.apple.url}")
public interface AppleFeignApi {

    @GetMapping("/keys")
    ApplePublicKeys getApplePublicKeys();

    @PostMapping(value = "/token", consumes = "application/x-www-form-urlencoded")
    AppleTokenRes getAppleToken(@RequestParam("client_secret") String clientSecret,
                         @RequestParam("code") String authCode,
                         @RequestParam("grant_type") String grantType,
                         @RequestParam("client_id") String clientId);

    @PostMapping(value = "/revoke", consumes = "application/x-www-form-urlencoded")
    void revokeUser(@RequestParam("client_id") String clientId,
                    @RequestParam("client_secret") String clientSecret,
                    @RequestParam("token") String accessToken,
                    @RequestParam("token_type_hint") String tokenTypeHint);
}
