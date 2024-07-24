package org.dateroad.course.api;


import static org.dateroad.common.ValidatorUtil.validateListSizeMax;
import static org.dateroad.common.ValidatorUtil.validateListSizeMin;
import static org.dateroad.common.ValidatorUtil.validateTagSize;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.dateroad.auth.argumentresolve.UserId;
import org.dateroad.code.FailureCode;
import org.dateroad.course.dto.request.CourseGetAllReq;
import org.dateroad.course.dto.request.CourseCreateReq;
import org.dateroad.course.dto.request.CoursePlaceGetReq;
import org.dateroad.course.dto.request.PointUseReq;
import org.dateroad.course.dto.request.TagCreateReq;
import org.dateroad.course.dto.response.CourseCreateRes;
import org.dateroad.course.dto.response.CourseGetAllRes;
import org.dateroad.course.dto.response.DateAccessGetAllRes;
import org.dateroad.course.service.AsyncService;
import org.dateroad.course.service.CourseService;
import org.dateroad.date.domain.Course;
import org.dateroad.date.dto.response.CourseGetDetailRes;
import org.dateroad.point.domain.TransactionType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController implements CourseApi {
    private final CourseService courseService;
    private final AsyncService asyncService;

    @GetMapping
    public ResponseEntity<CourseGetAllRes> getAllCourses(
            final @ModelAttribute @Valid CourseGetAllReq courseGetAllReq
    ) {
        CourseGetAllRes courseAll = courseService.getAllCourses(courseGetAllReq);
        return ResponseEntity.ok(courseAll);
    }

    @GetMapping("/sort")
    public ResponseEntity<CourseGetAllRes> getSortedCourses(final @RequestParam String sortBy) {
        CourseGetAllRes courseSortedRes = courseService.getSortedCourses(sortBy);
        return ResponseEntity.ok(courseSortedRes);
    }

    @GetMapping("/date-access")
    public ResponseEntity<DateAccessGetAllRes> getAllDataAccessCourse(final @UserId Long userId
    ) {
        DateAccessGetAllRes dateAccessGetAllRes = courseService.getAllDataAccessCourse(userId);
        return ResponseEntity.ok(dateAccessGetAllRes);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_OCTET_STREAM_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseCreateRes> createCourse(
            @UserId final Long userId,
            @RequestPart("course") @Valid final CourseCreateReq courseCreateReq,
            @RequestPart("tags") final List<TagCreateReq> tags,
            @RequestPart("places") final List<CoursePlaceGetReq> places,
            @RequestPart("images") final List<MultipartFile> images
    ) {
        validateListSizeMin(places, 1, FailureCode.WRONG_COURSE_PLACE_SIZE);
        validateListSizeMin(tags, 1, FailureCode.WRONG_TAG_SIZE);
        validateListSizeMax(tags, 3, FailureCode.WRONG_TAG_SIZE);
        validateListSizeMax(images, 10, FailureCode.WRONG_IMAGE_LIST_SIZE);
        Course course = courseService.createCourse(userId, courseCreateReq, places, images);
        asyncService.createCoursePlace(places, course);
        asyncService.createCourseTags(tags, course);
        asyncService.publishEvenUserPoint(userId, PointUseReq.of(100, TransactionType.POINT_GAINED, "코스 생성"));
        return ResponseEntity.status(
                HttpStatus.CREATED
        ).body(CourseCreateRes.of(course.getId()));
    }

    @GetMapping("/users")
    public ResponseEntity<DateAccessGetAllRes> getMyCourses(final @UserId Long userId) {
        DateAccessGetAllRes dateAccessGetAllRes = courseService.getMyCourses(userId);
        return ResponseEntity.ok(dateAccessGetAllRes);
    }

    @PostMapping("/{courseId}/date-access")
    public ResponseEntity<Void> openCourse(
            @UserId final Long userId,
            @PathVariable final Long courseId,
            @RequestBody @Valid final PointUseReq pointUseReq
    ) {
        courseService.openCourse(userId, courseId, pointUseReq);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseGetDetailRes> getCourseDetail(@UserId Long userId,
                                                              @PathVariable("courseId") Long courseId) {
        CourseGetDetailRes courseGetDetailRes = courseService.getCourseDetail(userId, courseId);
        return ResponseEntity.ok(courseGetDetailRes);
    }

    @PostMapping("/{courseId}/likes")
    public ResponseEntity<Void> createCourseLike(@UserId final Long userId,
                                                 @PathVariable final Long courseId) {
        courseService.createCourseLike(userId, courseId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{courseId}/likes")
    public ResponseEntity<Void> deleteCourseLike(@UserId final Long userId,
                                                 @PathVariable final Long courseId) {
        courseService.deleteCourseLike(userId, courseId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@UserId Long userId,
                                             @PathVariable final Long courseId) {
        courseService.deleteCourse(userId, courseId);
        return ResponseEntity.ok().build();
    }
}
