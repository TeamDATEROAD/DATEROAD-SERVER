package org.dateroad.course.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record CourseDtoRes(
        Long courseId,
        String thumbnail,
        String city,
        String title,
        int like,
        int cost,
        float duration
) {
    public static CourseDtoRes of(Long courseId,
                                  String thumbnail,
                                  String city,
                                  String title,
                                  int like,
                                  int cost,
                                  float duration) {
        return CourseDtoRes.builder()
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