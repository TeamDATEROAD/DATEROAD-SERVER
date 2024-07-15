package org.dateroad.course.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.dateroad.date.domain.Region;

@Builder(access = AccessLevel.PRIVATE)
public record CourseDtoGetRes(
        Long courseId,
        String thumbnail,
        Region.SubRegion city,
        String title,
        int like,
        int cost,
        float duration
) {
    public static CourseDtoGetRes of(Long courseId,
                                     String thumbnail,
                                     Region.SubRegion city,
                                     String title,
                                     int like,
                                     int cost,
                                     float duration) {
        return CourseDtoGetRes.builder()
                .courseId(courseId)
                .thumbnail(thumbnail)
                .city(city)
                .title(title)
                .like(like)
                .cost(cost)
                .duration(duration)
                .build();
    }
}