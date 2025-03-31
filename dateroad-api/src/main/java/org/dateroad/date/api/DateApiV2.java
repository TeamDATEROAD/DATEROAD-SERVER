package org.dateroad.date.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.dateroad.auth.argumentresolve.UserId;
import org.dateroad.date.dto.request.DateCreateReq;
import org.dateroad.date.dto.request.DateCreateReqV2;
import org.dateroad.date.dto.response.DateCreateRes;
import org.dateroad.date.dto.response.DateDetailResV2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "데이트 일정 관련 API V2")
@SecurityRequirement(name = "Authorization")
public interface DateApiV2 {
    @Operation(
            summary = "새로운 데이트 일정 생성V2",
            description = "주어진 사용자 ID와 날짜 생성 요청 정보를 기반으로 새로운 날짜를 생성합니다.",
            requestBody = @RequestBody(
                    description = "날짜 생성 요청 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DateCreateReq.class)
                    )
            )
    )
    ResponseEntity<DateCreateRes> createDate(@Parameter(hidden = true) @UserId final Long userId, @RequestBody @Valid final DateCreateReqV2 dateCreateReq);

    @Operation(
            summary = "데이트 일정 상세 조회 API V2",
            description = "데이트 일정 상세 조회 API V2",
            parameters = {
                    @Parameter(
                            name = "dateId",
                            description = "데이트 고유 ID",
                            example = "100"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "데이트 상세 정보 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DateDetailResV2.class)
                            )
                    )
            }
    )
    ResponseEntity<DateDetailResV2> getDateDetail(@Parameter(hidden = true) @UserId final Long userId, @PathVariable final Long dateId);
}
