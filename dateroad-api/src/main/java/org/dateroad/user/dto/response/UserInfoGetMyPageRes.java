package org.dateroad.user.dto.response;

import lombok.Builder;
import org.dateroad.tag.domain.DateTagType;

import java.util.List;

@Builder
public record UserInfoGetMyPageRes(
        String name,
        List<DateTagType> tags,
        int point,
        String imageUrl
) {
    public static UserInfoGetMyPageRes of(String name, List<DateTagType> tags, int point, String imageUrl) {
        return UserInfoGetMyPageRes.builder()
                .name(name)
                .tags(tags)
                .point(point)
                .imageUrl(imageUrl)
                .build();
    }
}
