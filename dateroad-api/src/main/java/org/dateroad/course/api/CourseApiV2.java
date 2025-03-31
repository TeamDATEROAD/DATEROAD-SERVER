package org.dateroad.course.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.dateroad.auth.argumentresolve.UserId;
import org.dateroad.course.dto.request.*;
import org.dateroad.course.dto.response.CourseCreateRes;
import org.dateroad.date.dto.response.CourseGetDetailResV2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Tag(name = "코스 관련 API V2")
@SecurityRequirement(name = "Authorization")
public interface CourseApiV2 {

    @Operation(
            summary = "코스 생성 API",
            requestBody = @RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = CourseCreateSwaggerDto.class)
                    )
            )
    )
    ResponseEntity<CourseCreateRes> createCourse(
            @UserId final Long userId,
            @RequestPart("course") @Valid final CourseCreateReqV2 courseCreateReq,
            @RequestPart("tags") @Validated @Size(min = 1, max = 3) final List<TagCreateReq> tags,
            @RequestPart("places") @Validated @Size(min = 1) final List<CoursePlaceGetReqV2> places,
            @RequestPart("images") @Validated @Size(min =1, max = 10) final List<MultipartFile> images
    ) throws ExecutionException, InterruptedException;

    @Operation(
            summary = "코스 정보 조회 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "코스 상세 정보입니다.",
                            content = @Content(
                                    schema = @Schema(implementation = CourseGetDetailResV2.class)
                            )
                    )
            }
    )
    ResponseEntity<CourseGetDetailResV2> getCourseDetail(
            @Parameter(hidden = true)
            @UserId Long userId,
            @PathVariable("courseId") Long courseId
    );
}
