package org.dateroad.course.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.dateroad.auth.argumentresolve.UserId;
import org.dateroad.code.FailureCode;
import org.dateroad.course.dto.request.CourseCreateReq;
import org.dateroad.course.dto.request.CoursePlaceGetReqV2;
import org.dateroad.course.dto.request.TagCreateReq;
import org.dateroad.course.dto.response.CourseCreateRes;
import org.dateroad.course.service.CourseService;
import org.dateroad.date.dto.response.CourseGetDetailResV2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.dateroad.common.ValidatorUtil.validateListSizeMax;
import static org.dateroad.common.ValidatorUtil.validateListSizeMin;

@RestController
@RequestMapping("/api/v2/courses")
@RequiredArgsConstructor
public class CourseControllerV2 {
    private final CourseService courseService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_OCTET_STREAM_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CourseCreateRes> createCourse(
            @UserId final Long userId,
            @RequestPart("course") @Valid final CourseCreateReq courseCreateReq,
            @RequestPart("tags") @Validated @Size(min = 1, max = 3) final List<TagCreateReq> tags,
            @RequestPart("places") @Validated @Size(min = 1) final List<CoursePlaceGetReqV2> places,
            @RequestPart("images") @Validated @Size(min =1, max = 10) final List<MultipartFile> images
    ) throws ExecutionException, InterruptedException {
        validateListSizeMin(places, 1, FailureCode.WRONG_COURSE_PLACE_SIZE);
        validateListSizeMin(tags, 1, FailureCode.WRONG_TAG_SIZE);
        validateListSizeMax(tags, 3, FailureCode.WRONG_TAG_SIZE);
        validateListSizeMax(images, 10, FailureCode.WRONG_IMAGE_LIST_SIZE);
        CourseCreateRes courseCreateRes = courseService.createCourseV2(userId, courseCreateReq, places, images, tags);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseCreateRes);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseGetDetailResV2> getCourseDetail(@UserId Long userId,
                                                              @PathVariable("courseId") Long courseId) {
        CourseGetDetailResV2 courseGetDetailRes = courseService.getCourseDetailV2(userId, courseId);
        return ResponseEntity.ok(courseGetDetailRes);
    }
}
