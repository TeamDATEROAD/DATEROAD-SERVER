package org.dateroad.course.api;


import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.dateroad.auth.argumentresolve.UserId;
import org.dateroad.course.dto.request.CourseGetAllReq;
import org.dateroad.course.dto.request.CourseCreateReq;
import org.dateroad.course.dto.request.CoursePlaceGetReq;
import org.dateroad.course.dto.request.TagCreateReq;
import org.dateroad.course.dto.response.CourseGetAllRes;
import org.dateroad.course.dto.response.DateAccessGetAllRes;
import org.dateroad.course.facade.CourseFacade;
import org.dateroad.course.service.CourseService;
import org.dateroad.date.domain.Course;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
    private final CourseFacade courseFacade;

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
    public ResponseEntity<Void> createCourse(
            @UserId final Long userId,
            @RequestPart("course") final CourseCreateReq courseCreateReq,
            @RequestPart("tags") final List<TagCreateReq> tags,
            @RequestPart("places") final List<CoursePlaceGetReq> places,
            @RequestPart("images") final List<MultipartFile> images
    ) {
        Course course = courseService.createCourse(userId, courseCreateReq, places, images);
        courseFacade.createCoursePlace(places, course);
        courseFacade.createCourseTags(tags, course);
        return ResponseEntity.created(
                URI.create(course.getId().toString())).build();
    }
}
