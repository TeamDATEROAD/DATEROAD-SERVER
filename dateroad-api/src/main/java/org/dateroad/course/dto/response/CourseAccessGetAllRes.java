package org.dateroad.course.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record CourseAccessGetAllRes(
        List<CourseDtoGetRes> courses
) {
    public static CourseAccessGetAllRes of(List<CourseDtoGetRes> dataAccessCourse) {
        return CourseAccessGetAllRes.builder()
                .courses(dataAccessCourse)
                .build();
    }
}