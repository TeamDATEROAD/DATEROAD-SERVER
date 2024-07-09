package org.dateroad.course.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record DateAccessGetAllRes(
        List<CourseDtoRes> courses
) {
    public static DateAccessGetAllRes of(List<CourseDtoRes> dataAccessCourse) {
        return DateAccessGetAllRes.builder()
                .courses(dataAccessCourse)
                .build();
    }
}