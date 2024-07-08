package org.dateroad.course.dto.response;

import java.util.List;
import org.dateroad.course.dto.response.CourseGetAllRes.CourseDtoRes;

public record DataAccessGetAllRes(
        List<CourseDtoRes> courses
) {
    public static DataAccessGetAllRes of(List<CourseDtoRes> dataAccessCourse) {
        return new DataAccessGetAllRes(dataAccessCourse);
    }
}