package org.dateroad.course.api;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.dateroad.auth.argumentresolve.UserId;
import org.dateroad.course.dto.request.CourseGetAllReq;
import org.dateroad.course.dto.request.CourseCreateReq;
import org.dateroad.course.dto.request.CoursePlaceGetReq;
import org.dateroad.course.dto.request.PointUseReq;
import org.dateroad.course.dto.request.TagCreateReq;
import org.dateroad.course.dto.response.CourseCreateRes;
import org.dateroad.course.dto.response.CourseGetAllRes;
import org.dateroad.course.dto.response.DateAccessGetAllRes;
import org.dateroad.course.facade.AsyncService;
import org.dateroad.course.service.CourseService;
import org.dateroad.date.domain.Course;
import org.dateroad.date.dto.response.CourseGetDetailRes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final AsyncService asyncService;

    @GetMapping
    public ResponseEntity<CourseGetAllRes> getAllCourses(
            final @ModelAttribute CourseGetAllReq courseGetAllReq
    ) {
        CourseGetAllRes courseAll = courseService.getAllCourses(courseGetAllReq);
        return ResponseEntity.ok(courseAll);
    }

    @GetMapping("/date-access")
    public ResponseEntity<DateAccessGetAllRes> getAllDataAccessCourse(
            final @UserId Long userId
    ) {
        DateAccessGetAllRes dateAccessGetAllRes = courseService.getAllDataAccessCourse(userId);
        return ResponseEntity.ok(dateAccessGetAllRes);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CourseCreateRes> createCourse(
            @UserId final Long userId,
            @RequestPart("course") final CourseCreateReq courseCreateReq,
            @RequestPart("tags") final List<TagCreateReq> tags,
            @RequestPart("places") final List<CoursePlaceGetReq> places,
            @RequestPart("images") final List<MultipartFile> images
    ) {
        Course course = courseService.createCourse(userId, courseCreateReq, places, images);
        asyncService.createCoursePlace(places, course);
        asyncService.createCourseTags(tags, course);
        return ResponseEntity.status(
                HttpStatus.CREATED
        ).body(CourseCreateRes.of(course.getId()));
    }

    @PostMapping("/{courseId}/date-access")
    public ResponseEntity<Void> openCourse(
            @UserId final Long userId,
            @PathVariable final Long courseId,
            @RequestBody final PointUseReq pointUseReq
    ) {
        courseService.openCourse(userId,courseId,pointUseReq);
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
    public ResponseEntity<Void> deleteCourseLike(@RequestHeader final Long userId,
                                                  @PathVariable final Long courseId) {
        courseService.deleteCourseLike(userId, courseId);
        return ResponseEntity.ok().build();
    }
}
