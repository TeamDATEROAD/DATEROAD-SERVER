package org.dateroad.feign.apple;

public record AppleTokenRes(
        String access_token,
        String token_type,
        int expires_in,
        String refresh_token,
        String id_token
) {
}
