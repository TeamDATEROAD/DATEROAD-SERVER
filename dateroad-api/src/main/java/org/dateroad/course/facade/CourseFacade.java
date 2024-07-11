package org.dateroad.course.facade;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.dateroad.Image.service.ImageService;
import org.dateroad.course.dto.request.CoursePlaceGetReq;
import org.dateroad.course.dto.request.TagCreateReq;
import org.dateroad.course.service.CoursePlaceService;
import org.dateroad.course.service.CourseTagService;
import org.dateroad.date.domain.Course;
import org.dateroad.image.domain.Image;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class CourseFacade {
    private final CoursePlaceService coursePlaceService;
    private final CourseTagService courseTagService;
    private final ImageService imageService;

    public Image findFirstByCourseOrderBySequenceAsc(final Course course) {
        return imageService.findFirstByCourseOrderBySequenceAsc(course);
    }

    public float findTotalDurationByCourseId(final Long id) {
        return coursePlaceService.findTotalDurationByCourseId(id);
    }

    @Transactional
    public String createImage(final List<MultipartFile> images, final Course course) {
        return imageService.saveImages(images, course);
    }

    @Async
    public void createCourseTags(final List<TagCreateReq> tags, final Course course) {
        courseTagService.createCourseTags(tags, course);
    }

    @Async
    public void createCoursePlace(final List<CoursePlaceGetReq> places, final Course course) {
        coursePlaceService.createCoursePlace(places, course);
    }
}
