package org.dateroad.course.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PROTECTED)
public record CourseCreateRes(
        Long courseId,
        int userPoint,
        Long userCourseCount
) {
    public static CourseCreateRes of(final Long courseId, final int userPoint, final Long userCourseCount) {
        return CourseCreateRes.builder()
                .courseId(courseId)
                .userPoint(userPoint)
                .userCourseCount(userCourseCount)
                .build();
    }
}
