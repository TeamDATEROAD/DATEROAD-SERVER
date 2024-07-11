package org.dateroad.course.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.dateroad.course.dto.request.TagCreateReq;
import org.dateroad.date.domain.Course;
import org.dateroad.tag.domain.CourseTag;
import org.dateroad.tag.repository.CourseTagRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseTagService {
    private final CourseTagRepository courseTagRepository;

    @Async
    @Transactional
    public void createCourseTags(final List<TagCreateReq> tags, final Course course) {
        List<CourseTag> coursePlaces = tags.stream()
                .map(tag -> CourseTag.create(
                        course,
                        tag.getTag()
                ))
                .collect(Collectors.toList());
        courseTagRepository.saveAllAndFlush(coursePlaces);
    }
}