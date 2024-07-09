package org.dateroad.course.api;


import lombok.RequiredArgsConstructor;
import org.dateroad.course.dto.request.CourseGetAllReq;
import org.dateroad.course.dto.response.CourseGetAllRes;
import org.dateroad.course.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<CourseGetAllRes> getAllCourse(
            @ModelAttribute CourseGetAllReq courseGetAllReq
    ) {
        CourseGetAllRes courseAll = courseService.getAllCourses(courseGetAllReq);
        return ResponseEntity.ok(courseAll);
    }
}
