package org.dateroad.course.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record DateAccessGetAllRes(
        List<CourseDtoRes> courses
) {
    public static DateAccessGetAllRes of(List<CourseDtoRes> dataAccessCourse) {
        return DateAccessGetAllRes.builder()
                .courses(dataAccessCourse)
                .build();
    }
}