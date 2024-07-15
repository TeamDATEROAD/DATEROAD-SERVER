package org.dateroad.course.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.dateroad.date.domain.Course;

@Builder(access = AccessLevel.PRIVATE)
public record CourseDtoGetRes(
        Long courseId,
        String thumbnail,
        String city,
        String title,
        int like,
        int cost,
        float duration
) {
    public static CourseDtoGetRes of(Course course,
                                     String thumbnail,
                                     int like,
                                     float duration) {
        return CourseDtoGetRes.builder()
                .courseId(course.getId())
                .thumbnail(thumbnail)
                .city(course.getCity())
                .title(course.getCity())
                .like(like)
                .cost(course.getCost())
                .duration(duration)
                .build();
    }
}