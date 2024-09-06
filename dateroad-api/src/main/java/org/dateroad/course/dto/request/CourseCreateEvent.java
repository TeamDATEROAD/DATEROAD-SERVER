package org.dateroad.course.dto.request;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.dateroad.date.domain.Course;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class CourseCreateEvent extends ApplicationEvent {
    private final Course course;
    private final Long userId;
    private final List<CoursePlaceGetReq> places;
    private final List<MultipartFile> images;
    private final List<TagCreateReq> tags;

    public CourseCreatedEvent(Object source, Course course, Long userId,
                              List<CoursePlaceGetReq> places, List<MultipartFile> images,
                              List<TagCreateReq> tags) {
        super(source);
        this.course = course;
        this.userId = userId;
        this.places = places;
        this.images = images;
        this.tags = tags;
    }
}
