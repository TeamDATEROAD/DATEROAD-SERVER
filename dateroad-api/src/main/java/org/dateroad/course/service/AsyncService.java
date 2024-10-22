package org.dateroad.course.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dateroad.code.FailureCode;
import org.dateroad.course.dto.CourseWithPlacesAndTagsDto;
import org.dateroad.course.dto.request.CourseCreateEvent;
import org.dateroad.course.dto.request.PointUseReq;
import org.dateroad.date.domain.Course;
import org.dateroad.exception.DateRoadException;
import org.dateroad.image.domain.Image;
import org.dateroad.image.service.ImageService;
import org.dateroad.place.domain.CoursePlace;
import org.dateroad.point.event.MessageDto.FreeMessageDTO;
import org.dateroad.point.event.MessageDto.PointMessageDTO;
import org.dateroad.tag.domain.CourseTag;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class AsyncService {
    private final CoursePlaceService coursePlaceService;
    private final CourseTagService courseTagService;
    private final ImageService imageService;
    private final RedisTemplate<String, String> redistemplateForCluster;
    public List<Image> createImage(final List<MultipartFile> images, final Course course) {
        return imageService.saveImages(images, course);
    }

    @Transactional
    public RecordId publishEvenUserPoint(final Long userId, PointUseReq pointUseReq) {
        try {
            PointMessageDTO pointMessage = PointMessageDTO.of(userId, pointUseReq);
            return redistemplateForCluster.opsForStream().add("coursePoint", pointMessage.toMap());
        } catch (QueryTimeoutException e) {
            log.error("Redis command timed out for userId: {} - Retrying...", userId, e);
            throw new DateRoadException(FailureCode.REDIS_CONNECTION_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while publishing point event for userId: {}", userId, e);
            throw new DateRoadException(FailureCode.REDIS_CONNECTION_ERROR);
        }
    }

    @Transactional
    public void publishEventUserFree(final Long userId) {
        try {
            FreeMessageDTO freeMessage = FreeMessageDTO.of(userId);
            redistemplateForCluster.opsForStream().add("courseFree", freeMessage.toMap());
        } catch (QueryTimeoutException e) {
            log.error("Redis command timed out for userId: {} - Retrying...", userId, e);
            throw new DateRoadException(FailureCode.REDIS_CONNECTION_ERROR);
        } catch (Exception e) {
            log.error("Unexpected error while publishing free event for userId: {}", userId, e);
            throw new DateRoadException(FailureCode.REDIS_CONNECTION_ERROR);
        }
    }

    @Transactional
    public CourseWithPlacesAndTagsDto runAsyncTasks(CourseCreateEvent event) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            CompletableFuture<List<CoursePlace>> placeFuture = CompletableFuture.supplyAsync(() -> coursePlaceService.createCoursePlace(event.getPlaces(), event.getCourse()), executor);
            CompletableFuture<List<CourseTag>> tagFuture = CompletableFuture.supplyAsync(() -> courseTagService.createCourseTags(event.getTags(), event.getCourse()), executor);
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(placeFuture, tagFuture);
            allFutures.join();
            return CourseWithPlacesAndTagsDto.of(placeFuture.join(), tagFuture.join());
        }
    }

    public String createCourseImages(List<MultipartFile> images, Course course) {
        List<Image> imageList = createImage(images, course);
        return imageList.getFirst().getImageUrl();
    }
}
