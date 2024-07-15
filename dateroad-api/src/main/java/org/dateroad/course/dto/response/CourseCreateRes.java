package org.dateroad.course.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PROTECTED)
public record CourseCreateRes(
        Long courseId
) {
    public static CourseCreateRes of(final Long courseId) {
        return CourseCreateRes.builder()
                .courseId(courseId)
                .build();
    }
}
