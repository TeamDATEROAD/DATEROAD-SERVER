package org.dateroad.course.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

@Builder
public record CourseGetAllRes(
        List<CourseDtoRes> courses
) {


    public static CourseGetAllRes of(List<CourseDtoRes> courses) {
        return CourseGetAllRes.builder()
                .courses(courses)
                .build();
    }

}
