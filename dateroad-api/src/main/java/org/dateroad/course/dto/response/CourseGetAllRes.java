package org.dateroad.course.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder(access = AccessLevel.PRIVATE)
public record CourseGetAllRes(
        List<CourseDtoGetRes> courses
) {
    public static CourseGetAllRes of(List<CourseDtoGetRes> courses) {
        return CourseGetAllRes.builder()
                .courses(courses)
                .build();
    }
}
