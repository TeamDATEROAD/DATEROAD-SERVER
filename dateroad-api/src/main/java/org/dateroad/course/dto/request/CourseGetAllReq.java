package org.dateroad.course.dto.request;

public record CourseGetAllReq(
        String country,
        String city,
        Integer cost
) {
}
