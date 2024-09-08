package org.dateroad.course.service;

import static java.lang.Thread.startVirtualThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.DateRoadException;
import org.dateroad.image.service.ImageService;
import org.dateroad.course.dto.request.CoursePlaceGetReq;
import org.dateroad.course.dto.request.PointUseReq;
import org.dateroad.course.dto.request.TagCreateReq;
import org.dateroad.date.domain.Course;
import org.dateroad.image.domain.Image;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final RedisTemplate<String, String> redistemplateForCluster;

    public List<Image> createImage(final List<MultipartFile> images, final Course course) {
        return imageService.saveImages(images, course);
    }

    public void createCourseTags(final List<TagCreateReq> tags, final Course course) {
        courseTagService.createCourseTags(tags, course);
    }

    public void createCoursePlace(final List<CoursePlaceGetReq> places, final Course course) {
        coursePlaceService.createCoursePlace(places, course);
    }

    public void publishEvenUserPoint(final Long userId, PointUseReq pointUseReq) {
        Map<String, String> fieldMap = new HashMap<>();
        fieldMap.put("userId", userId.toString());
        fieldMap.put("point", String.valueOf(pointUseReq.getPoint()));
        fieldMap.put("type", pointUseReq.getType().name());
        fieldMap.put("description", pointUseReq.getDescription());
        redistemplateForCluster.opsForStream().add("coursePoint", fieldMap);
    }

    public void publishEventUserFree(final Long userId) {
        Map<String, String> fieldMap = new HashMap<>();
        fieldMap.put("userId", userId.toString());
        redistemplateForCluster.opsForStream().add("courseFree", fieldMap);
    }

    @Transactional
    public void runAsyncTasks(List<CoursePlaceGetReq> places, List<TagCreateReq> tags,
                                Course saveCourse)
            throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        final boolean[] hasError = {false};  // 에러 발생 여부 확인
        threads.add(runAsyncTaskWithExceptionHandling(() -> {
            createCoursePlace(places, saveCourse);
        }, hasError));
        threads.add(runAsyncTaskWithExceptionHandling(() -> {
            createCourseTags(tags, saveCourse);
        }, hasError));
        for (Thread thread : threads) {
            thread.join();
        }
        if (hasError[0]) {
            throw new RuntimeException("코스 생성중 오류 발생");  // 예외 발생 시 전체 작업 실패 처리
        }
    }

    @Transactional
    public String createCourseImages(List<MultipartFile> images, Course course) {
        List<Image> imageList = createImage(images, course);
        return imageList.getFirst().getImageUrl();
    }

    public Thread runAsyncTaskWithExceptionHandling(Runnable task, boolean[] hasError) {
        return startVirtualThread(() -> {
            try {
                task.run();
            } catch (Exception e) {
                hasError[0] = true;
                throw new DateRoadException(FailureCode.COURSE_CREATE_ERROR);
            }
        });
    }
}
