package org.dateroad.date.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dateroad.auth.argumentresolve.UserId;
import org.dateroad.date.dto.request.DateCreateReq;
import org.dateroad.date.dto.response.DateDetailRes;
import org.dateroad.date.dto.response.DateGetNearestRes;
import org.dateroad.date.dto.response.DatesGetRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "데이트 일정 관련 API")
@SecurityRequirement(name = "Authorization")
public interface DateApi {
    @Operation(
            summary = "새로운 데이트 일정 생성",
            description = "주어진 사용자 ID와 날짜 생성 요청 정보를 기반으로 새로운 날짜를 생성합니다.",
            requestBody = @RequestBody(
                    description = "날짜 생성 요청 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DateCreateReq.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "title": "서울 여행",
                                        "date": "2024.07.15",
                                        "startAt": "09:00 AM",
                                        "tags": [
                                            {
                                                "tag": "TRAVEL"
                                            },
                                            {
                                                "tag": "VACATION"
                                            }
                                        ],
                                        "country": "대한민국",
                                        "city": "서울",
                                        "places": [
                                            {
                                                "title": "경복궁",
                                                "duration": 2.5,
                                                "sequence": 1
                                            },
                                            {
                                                "title": "남산타워",
                                                "duration": 1.5,
                                                "sequence": 2
                                            }
                                        ]
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "데이트가 성공적으로 생성되었습니다.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청입니다.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "인증 오류",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content
                    )
            }
    )
    ResponseEntity<Void> createDate(@Parameter(hidden = true)
                                    @UserId final Long userId,
                                    @RequestBody final DateCreateReq dateCreateReq);

    @Operation(
            summary = "지난 & 다가올 데이트 일정 전체 조회 API",
            description = "지난 & 다가올 데이트 일정 전체 조회 API",
            parameters = {
                    @Parameter(
                            name = "time",
                            description = "지난 데이트 vs 다가오는 데이트 구분 파라미터",
                            required = true,
                            schema = @Schema(type = "string", allowableValues = {"PAST", "FUTURE"}),
                            example = "PAST"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "날짜 목록이 성공적으로 조회되었습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DatesGetRes.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "dates": [
                                                    {
                                                        "dateId": 1,
                                                        "title": "서울 여행",
                                                        "date": "2024.07.15",
                                                        "city": "Seoul",
                                                        "tags": [
                                                            {
                                                                "tag": "여행"
                                                            },
                                                            {
                                                                "tag": "휴가"
                                                            }
                                                        ],
                                                        "dDay": 10
                                                    },
                                                    {
                                                        "dateId": 2,
                                                        "title": "부산 여행",
                                                        "date": "2024.08.20",
                                                        "city": "Busan",
                                                        "tags": [
                                                            {
                                                                "tag": "여행"
                                                            },
                                                            {
                                                                "tag": "바다"
                                                            }
                                                        ],
                                                        "dDay": 25
                                                    }
                                                ]
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청입니다.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "인증 오류",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content
                    )
            }
    )
    ResponseEntity<DatesGetRes> getDates(@Parameter(hidden = true)
                                         @UserId final Long userId,
                                         @RequestParam final String time);

    @Operation(
            summary = "데이트 일정 상세 조회 API",
            description = "데이트 일정 상세 조회 API.",
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
                                    schema = @Schema(implementation = DateDetailRes.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "dateId": 100,
                                                "title": "서울 여행",
                                                "startAt": "09:00 AM",
                                                "city": "Seoul",
                                                "tags": [
                                                    {
                                                        "tag": "여행"
                                                    },
                                                    {
                                                        "tag": "휴가"
                                                    }
                                                ],
                                                "date": "2024.07.15",
                                                "places": [
                                                    {
                                                        "title": "경복궁",
                                                        "duration": 2.5,
                                                        "sequence": 1
                                                    },
                                                    {
                                                        "title": "남산타워",
                                                        "duration": 1.5,
                                                        "sequence": 2
                                                    }
                                                ],
                                                "dDay": 10
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청입니다.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "인증 오류",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content
                    )
            }
    )
    ResponseEntity<DateDetailRes> getDateDetail(@Parameter(hidden = true)
                                                @UserId final Long userId,
                                                @PathVariable final Long dateId);

    @Operation(
            summary = "데이트 삭제 API",
            description = "데이트 일정 삭제 API.",
            parameters = {
                    @Parameter(
                            name = "dateId",
                            description = "데이트 고유 ID",
                            required = true,
                            schema = @Schema(type = "long"),
                            example = "100"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "데이트 삭제 성공",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청입니다.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "인증 오류",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content
                    )
            }
    )
    ResponseEntity<Void> deleteDate(@Parameter(hidden = true)
                                    @UserId final Long userId,
                                    @PathVariable final Long dateId);


    @Operation(
            summary = "가장 가까운 날짜 정보 조회",
            description = "가장 가까운 날짜 정보 조회",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "가장 가까운 날짜 정보가 성공적으로 조회되었습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DateGetNearestRes.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "dateId": 1,
                                                "dDay": 2,
                                                "dateName": "마라톤 대회",
                                                "month": 7,
                                                "day": 15,
                                                "startAt": "08:00 AM"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청입니다.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "인증 오류",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "해당 데이트를 찾을 수 없습니다.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content
                    )
            }
    )
    ResponseEntity<DateGetNearestRes> getNearestDate(@Parameter(hidden = true)
                                                     @UserId final Long userId);

}
