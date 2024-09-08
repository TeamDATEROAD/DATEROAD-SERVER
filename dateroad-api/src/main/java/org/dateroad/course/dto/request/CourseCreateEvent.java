package org.dateroad.course.dto.request;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dateroad.date.domain.Course;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CourseCreateEvent{
    private Course course;
    private List<CoursePlaceGetReq> places;
    private List<TagCreateReq> tags;

    public static CourseCreateEvent of(Course course,
                              List<CoursePlaceGetReq> places,
                              List<TagCreateReq> tags) {
        return CourseCreateEvent.builder()
                .course(course)
                .places(places)
                .tags(tags)
                .build();
    }
}
