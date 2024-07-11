package org.dateroad.course.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record DateAccessGetAllRes(
        List<CourseDtoGetRes> courses
) {
    public static DateAccessGetAllRes of(List<CourseDtoGetRes> dataAccessCourse) {
        return DateAccessGetAllRes.builder()
                .courses(dataAccessCourse)
                .build();
    }
}