package org.dateroad.course.dto.response;

import java.util.List;

public record CourseGetAllRes(
        List<CourseDtoRes> courses
) {
    public record CourseDtoRes(
            Long courseId,
            String thumbnail,
            String city,
            String title,
            int like,
            int cost,
            int duration
    ) {
    }

    public static CourseGetAllRes of(List<CourseDtoRes> courses) {
        return new CourseGetAllRes(courses);
    }
}