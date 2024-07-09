package org.dateroad.course.dto.response;

import lombok.Builder;

@Builder
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