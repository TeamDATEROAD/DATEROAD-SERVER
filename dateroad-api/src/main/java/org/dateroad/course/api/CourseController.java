package org.dateroad.course.api;


import java.net.URI;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final AsyncService asyncService;

    @GetMapping
    public ResponseEntity<CourseGetAllRes> getAllCourse(
            @ModelAttribute CourseGetAllReq courseGetAllReq
    ) {
        CourseGetAllRes courseAll = courseService.getAllCourses(courseGetAllReq);
        return ResponseEntity.ok(courseAll);
    }

    @GetMapping("/date-access")
    public ResponseEntity<DateAccessGetAllRes> getAllDataAccesCourse(
            @UserId Long userId
    ) {
        DateAccessGetAllRes dateAccessGetAllRes = courseService.getAllDataAccessCourse(userId);
        return ResponseEntity.ok(dateAccessGetAllRes);
    }

    @PostMapping(value = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
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
}
