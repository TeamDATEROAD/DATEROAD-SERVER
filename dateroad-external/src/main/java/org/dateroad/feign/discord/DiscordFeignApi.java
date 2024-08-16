package org.dateroad.feign.discord;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${feign.discord.name}", url = "${feign.discord.webhook.url}")
public interface DiscordFeignApi {

    @PostMapping
    void sendMessage(@RequestBody final DiscordFeignReq discordFeignReq);
}
