package org.dateroad.course.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record CourseGetAllRes(
        List<CourseDtoGetRes> courses
) {
    public static CourseGetAllRes of(List<CourseDtoGetRes> courses) {
        return CourseGetAllRes.builder()
                .courses(courses)
                .build();
    }
}
