package org.dateroad.course.api;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import java.net.MulticastSocket;
import java.net.URI;
import java.util.List;
import javax.xml.crypto.OctetStreamData;
import lombok.RequiredArgsConstructor;
import org.dateroad.Image.dto.request.ImageReq;
import org.dateroad.auth.argumentresolve.UserId;
import org.dateroad.course.dto.request.CourseGetAllReq;
import org.dateroad.course.dto.request.CourseCreateReq;
import org.dateroad.course.dto.response.CourseGetAllRes;
import org.dateroad.course.dto.response.DateAccessGetAllRes;
import org.dateroad.course.service.CourseService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
//@RequestMapping("/api/v1/courses")
@RequestMapping("/api/v1")
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

    @GetMapping("/date-access")
    public ResponseEntity<DateAccessGetAllRes> getAllDataAccesCourse(
            @UserId Long userId
    ) {
        DateAccessGetAllRes dateAccessGetAllRes = courseService.getAllDataAccessCourse(userId);
        return ResponseEntity.ok(dateAccessGetAllRes);
    }

    @PostMapping("/c")
    public ResponseEntity<Void> createCourse(
            @RequestHeader Long userId,
            @ModelAttribute CourseCreateReq courseCreateReq
    ) {
        System.out.println(courseCreateReq);
        return ResponseEntity.created(
                URI.create(courseService.createCourse(userId, courseCreateReq))).build();
    }
}
