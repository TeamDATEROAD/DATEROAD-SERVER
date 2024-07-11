package org.dateroad.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserInfoMainRes(
        String name,
        int point,
        String image
) {
    public static UserInfoMainRes of(String name, int point, String image) {
        return UserInfoMainRes.builder()
                .name(name)
                .point(point)
                .image(image)
                .build();
    }
}
