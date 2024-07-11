package org.dateroad.feign.apple;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "${feign.apple.name}", url = "${feign.apple.url}")
public interface AppleFeignApi {

    @GetMapping("/keys")
    ApplePublicKeys getApplePublicKeys();
}
