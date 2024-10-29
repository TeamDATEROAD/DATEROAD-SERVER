package org.dateroad.course.service;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.dateroad.course.dto.request.TagCreateReq;
import org.dateroad.date.domain.Course;
import org.dateroad.tag.domain.CourseTag;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseTagService {
    public List<CourseTag> createCourseTags(final List<TagCreateReq> tags, final Course course) {
        return tags.stream()
                .map(tag -> CourseTag.create(course, tag.getTag()))
                .toList();
    }
}