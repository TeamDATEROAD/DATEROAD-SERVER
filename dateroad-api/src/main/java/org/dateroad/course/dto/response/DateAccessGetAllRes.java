package org.dateroad.course.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record DateAccessGetAllRes(
        List<CourseDtoGetRes> courses
) {
    public static DateAccessGetAllRes of(List<CourseDtoGetRes> dataAccessCourse) {
        return DateAccessGetAllRes.builder()
                .courses(dataAccessCourse)
                .build();
    }
}