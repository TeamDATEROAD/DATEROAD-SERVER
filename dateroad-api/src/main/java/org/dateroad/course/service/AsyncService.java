package org.dateroad.course.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.dateroad.image.service.ImageService;
import org.dateroad.course.dto.request.CoursePlaceGetReq;
import org.dateroad.course.dto.request.PointUseReq;
import org.dateroad.course.dto.request.TagCreateReq;
import org.dateroad.date.domain.Course;
import org.dateroad.image.domain.Image;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class AsyncService {
    private final CoursePlaceService coursePlaceService;
    private final CourseTagService courseTagService;
    private final ImageService imageService;
    private final StringRedisTemplate redisTemplate;

    @Transactional
    public List<Image> createImage(final List<MultipartFile> images, final Course course) {
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

    public void publishEvenUserPoint(final Long userId, PointUseReq pointUseReq) {
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("userId", userId.toString());
        fieldMap.put("point", Integer.toString(pointUseReq.getPoint()));
        fieldMap.put("type", pointUseReq.getType().toString());
        redisTemplate.opsForStream().add("coursePoint", fieldMap);
    }

    public void publishEventUserFree(final Long userId) {
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("userId", userId.toString());
        redisTemplate.opsForStream().add("courseFree", fieldMap);
    }
}
