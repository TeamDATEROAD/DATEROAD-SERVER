package org.dateroad.course.dto.request;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dateroad.date.domain.Course;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CourseCreateEventV2{
    private Course course;
    private List<CoursePlaceGetReqV2> places;
    private List<TagCreateReq> tags;

    public static CourseCreateEventV2 of(Course course,
                                       List<CoursePlaceGetReqV2> places,
                                       List<TagCreateReq> tags) {
        return CourseCreateEventV2.builder()
                .course(course)
                .places(places)
                .tags(tags)
                .build();
    }
}
