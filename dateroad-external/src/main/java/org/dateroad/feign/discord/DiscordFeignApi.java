package org.dateroad.feign.discord;

import org.dateroad.code.EventCode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${feign.discord.name}", url = "${feign.discord.webhook.url}")
public interface DiscordFeignApi {

    @PostMapping
    void sendMessage(@RequestBody DiscordFeignReq discordFeignReq);

}
