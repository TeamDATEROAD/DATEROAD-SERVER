package org.dateroad.feign.discord;

public record DiscordFeignReq(
        String content
) {
    public static DiscordFeignReq of(final String content) {
        return new DiscordFeignReq(content);
    }
}
