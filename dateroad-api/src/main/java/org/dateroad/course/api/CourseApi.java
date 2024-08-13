package org.dateroad.course.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.dateroad.auth.argumentresolve.UserId;
import org.dateroad.course.dto.request.CourseCreateReq;
import org.dateroad.course.dto.request.CourseCreateSwaggerDto;
import org.dateroad.course.dto.request.CourseGetAllReq;
import org.dateroad.course.dto.request.CoursePlaceGetReq;
import org.dateroad.course.dto.request.PointUseReq;
import org.dateroad.course.dto.request.TagCreateReq;
import org.dateroad.course.dto.response.CourseCreateRes;
import org.dateroad.course.dto.response.CourseGetAllRes;
import org.dateroad.course.dto.response.DateAccessGetAllRes;
import org.dateroad.date.dto.response.CourseGetDetailRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "코스 관련 API")
@SecurityRequirement(name = "Authorization")
public interface CourseApi {
    @Operation(
            summary = "코스 전체 조회 API",
            parameters = {
                    @Parameter(
                            name = "country",
                            description = "데이트 코스 지역 대분류",
                            required = false,
                            schema = @Schema(type = "string", allowableValues = {"SEOUL", "GYEONGGI", "INCHEON"}),
                            example = "SEOUL"
                    ),
                    @Parameter(
                            name = "city",
                            description = "데이트 코스 지역 소분류",
                            required = false,
                            schema = @Schema(type = "string", allowableValues = {
                                    "SEOUL_ENTIRE", "GANGNAM_SEOCHO", "JAMSIL_SONGPA_GANGDONG",
                                    "KONDAE_SEONGSU_SEONGDONG",
                                    "JONGNO_JUNGRO", "HONGDAE_HAPJEONG_MAPO", "YEONGDEUNGPO_YEOUIDO",
                                    "YONGSAN_ITAEWON_HANNAM",
                                    "YANGCHEON_GANGSEO_GURO", "GWANGIN_JUNGBANG", "GWANAK_DONGJAK_GEUMCHEON",
                                    "GYEONGGI_ENTIRE",
                                    "SEONGNAM", "SUWON", "GOYANG_PAJU", "GIMPO", "YONGIN_HWASEONG", "ANYANG_GWACHEON",
                                    "POCHEON_YANGJU", "NAMYANGJU_UIJEONGBU", "GWANGJU_ICHEON_YEOJU",
                                    "GAPYEONG_YANGPYEONG",
                                    "GUNPO_UIWANG", "HANAM_GURI", "SIHEUNG_GWANGMYEONG", "BUCHEON_ANSHAN",
                                    "DONGDUCHEON_YEONCHEON", "PYEONGTAEK_OSAN_ANSEONG", "INCHEON_ENTIRE"
                            }),
                            example = "GANGNAM_SEOCHO"
                    ),
                    @Parameter(
                            name = "cost",
                            description = "필터링할 가격",
                            required = false,
                            schema = @Schema(type = "int"),
                            example = "50000"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = CourseGetAllRes.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "courses": [
                                                    {
                                                        "courseId": 1,
                                                        "thumbnail": "https://example.com/image1.jpg",
                                                        "city": "건대/상수/왕십리",,
                                                        "title": "Introduction to Java",
                                                        "like": 100,
                                                        "cost": 500,
                                                        "duration": 10
                                                    },
                                                    {
                                                        "courseId": 2,
                                                        "thumbnail": "https://example.com/image2.jpg",
                                                        "city": ""건대/상수/왕십리",,
                                                        "title": "Advanced Spring",
                                                        "like": 150,
                                                        "cost": 700,
                                                        "duration": 15
                                                    }
                                                ]
                                            }
                                            """)
                            ),
                            description = "요청이 성공했습니다."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 값이 올바르지 않습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "405",
                            description = "잘못된 HTTP method 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content)})
    ResponseEntity<CourseGetAllRes> getAllCourses(
            @Parameter(required = false) final @ModelAttribute @Valid CourseGetAllReq courseGetAllReq
    );

    @Operation(
            summary = "정렬된 코스 내역 조회 API",
            parameters = {
                    @Parameter(
                            name = "sortBy",
                            description = "데이트 코스를 인기순/최신순 으로 조회하는 Api입니다.",
                            required = true,
                            schema = @Schema(type = "string", allowableValues = {"POPULAR", "LATEST"}),
                            example = "POPULAR"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = CourseGetAllRes.class),
                                    examples = @ExampleObject(value = """
                                            {
                                             	"courses": [
                                                    {
                                                        "courseId":1,
                                                        "thumbnail": "www.asdfasdfds.jpg",
                                                        "title": "5년차 장기연애 커플이 보장하는
                                                                    성수동 당일치기 데이트 코스",
                                                        "city": "건대/상수/왕십리",
                                                        "like" : 3,
                                                        "cost" : 10,
                                                        "duration" : 10
                                                    },
                                                    {
                                                        "courseId" : 2,
                                                        "thumbnail": "www.asdfasdfds.jpg",	
                                                        "title": "5년차 장기연애 커플이 보장하는
                                                                    성수동 당일치기 데이트 코스",
                                                        "city": "건대/상수/왕십리",
                                                        "like" : 3,
                                                        "cost" : 10,
                                                        "duration" : 10
                                                    },
                                                    {
                                                        "courseId":3,
                                                        "thumbnail": "www.asdfasdfds.jpg",
                                                        "title": "5년차 장기연애 커플이 보장하는
                                                                    성수동 당일치기 데이트 코스",
                                                        "city": "건대/상수/왕십리",
                                                        "like" : 3,
                                                        "cost" : 10,
                                                        "duration" : 10
                                                    }
                                             }
                                            """)
                            ),
                            description = "요청이 성공했습니다."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 값이 올바르지 않습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "405",
                            description = "잘못된 HTTP method 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content)})
    ResponseEntity<CourseGetAllRes> getSortedCourses(final @RequestParam String sortBy);

    @Operation(
            summary = "열람한 코스 내역 조회 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = DateAccessGetAllRes.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "courses": [
                                                    {
                                                        "courseId": 1,
                                                        "thumbnail": "https://example.com/image1.jpg",
                                                        "city": ""건대/상수/왕십리",",
                                                        "title": "Java Programming",
                                                        "like": 100,
                                                        "cost": 500,
                                                        "duration": 10.0
                                                    },
                                                    {
                                                        "courseId": 2,
                                                        "thumbnail": "https://example.com/image2.jpg",
                                                        "city": ""건대/상수/왕십리",",
                                                        "title": "Spring Framework",
                                                        "like": 150,
                                                        "cost": 700,
                                                        "duration": 15.0
                                                    }
                                                ]
                                            }
                                            """)
                            ),
                            description = "요청이 성공했습니다."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 값이 올바르지 않습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "405",
                            description = "잘못된 HTTP method 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content)})
    ResponseEntity<DateAccessGetAllRes> getAllDataAccessCourse(
            @Parameter(hidden = true) final @UserId Long userId
    );

    @Operation(
            summary = "코스 생성 API",
            requestBody = @RequestBody(
                    content = @Content(
                            schema = @Schema(implementation = CourseCreateSwaggerDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "title": "Seoul Day Tour",
                                        "description": "A day tour visiting main attractions in Seoul",
                                        "city": "SEOUL",
                                        "country": "SEOUL_ENTIRE",
                                        "date": "2024.07.14",
                                        "startAt": "09:00 AM",
                                        "cost": 20000,
                                        "places": [
                                            {
                                                "sequence": 1,
                                                "title": "Museum",
                                                "duration": 1.5
                                            },
                                            {
                                                "sequence": 2,
                                                "title": "Park",
                                                "duration": 2.0
                                            }
                                        ],
                                        "tags": [
                                            {
                                                "tag": "DRIVE"
                                            },
                                            {
                                                "tag": "HEALING"
                                            }
                                        ]
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(
                                    schema = @Schema(implementation = CourseCreateRes.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "courseId": 1
                                            }
                                            """)
                            ),
                            description = "코스가 성공적으로 생성되었습니다."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 값이 올바르지 않습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "405",
                            description = "잘못된 HTTP method 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content)})
    ResponseEntity<CourseCreateRes> createCourse(
            @UserId final Long userId,
            @RequestPart("course") @Valid final CourseCreateReq courseCreateReq,
            @RequestPart("tags") final List<TagCreateReq> tags,
            @RequestPart("places") final List<CoursePlaceGetReq> places,
            @RequestPart("images") final List<MultipartFile> images
    );


    @Operation(
            summary = "내가 만든 코스 조회 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = DateAccessGetAllRes.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "courses": [
                                                    {
                                                        "courseId": 1,
                                                        "thumbnail": "https://example.com/image1.jpg",
                                                        "city": "서울 전체",
                                                        "title": "Java Programming",
                                                        "like": 100,
                                                        "cost": 500,
                                                        "duration": 10.0
                                                    },
                                                    {
                                                        "courseId": 2,
                                                        "thumbnail": "https://example.com/image2.jpg",
                                                        "city": "서울 전체",
                                                        "title": "Spring Framework",
                                                        "like": 150,
                                                        "cost": 700,
                                                        "duration": 15.0
                                                    }
                                                ]
                                            }
                                            """)
                            ),
                            description = "요청이 성공했습니다."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 값이 올바르지 않습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "405",
                            description = "잘못된 HTTP method 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content)})
    ResponseEntity<DateAccessGetAllRes> getMyCourses(
            @Parameter(hidden = true) final @UserId Long userId
    );

    @Operation(
            summary = "코스 열람하기 API",
            description = "주어진 사용자 ID와 코스 ID를 기반으로 코스를 오픈합니다. 포인트가 없을시 정보를 보내지 않을시 무료열람에서 자동 차감됩니다.",
            requestBody = @RequestBody(
                    description = "포인트 사용 요청 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PointUseReq.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "point": 100,
                                        "type": "POINT_USED",
                                        "description": "포인트획득"
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content,
                            description = "요청이 성공했습니다."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 값이 올바르지 않습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "405",
                            description = "잘못된 HTTP method 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content)})
    ResponseEntity<Void> openCourse(
            @Parameter(hidden = true)
            @UserId final Long userId,
            @PathVariable final Long courseId,
            @RequestBody @Valid final PointUseReq pointUseReq
    );

    @Operation(
            summary = "코스 정보 조회 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "코스 상세 정보입니다.",
                            content = @Content(
                                    schema = @Schema(implementation = CourseGetDetailRes.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "courseId": 101,
                                                "images": [
                                                    {
                                                        "imageUrl": "https://example.com/image1.jpg",
                                                        "sequence": 1
                                                    },
                                                    {
                                                        "imageUrl": "https://example.com/image2.jpg",
                                                        "sequence": 2
                                                    }
                                                ],
                                                "like": 120,
                                                "totalTime": 3.5,
                                                "date": "2024.07.14",
                                                "city": "종로/중구",
                                                "title": "Seoul Day Tour",
                                                "description": "A day tour visiting main attractions in Seoul",
                                                "startAt": "09:00 AM",
                                                "places": [
                                                    {
                                                        "sequence": 1,
                                                        "title": "Museum",
                                                        "duration": 1.5
                                                    },
                                                    {
                                                        "sequence": 2,
                                                        "title": "Park",
                                                        "duration": 2.0
                                                    }
                                                ],
                                                "totalCost": 20000,
                                                "tags": [
                                                    {
                                                        "tag": "DRIVE"
                                                    },
                                                    {
                                                        "tag": "WORKSHOP"
                                                    }
                                                ],
                                                "isAccess": true,
                                                "free": 2,
                                                "totalPoint": 150,
                                                "isCourseMine": true,
                                                "isUserLiked": false
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 값이 올바르지 않습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "405",
                            description = "잘못된 HTTP method 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content)})
    ResponseEntity<CourseGetDetailRes> getCourseDetail(
            @Parameter(hidden = true)
            @UserId Long userId,
            @PathVariable("courseId") Long courseId
    );

    @Operation(
            summary = "코스 좋아요 등록 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "코스 좋아요 등록 API",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 값이 올바르지 않습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "405",
                            description = "잘못된 HTTP method 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content)})
    ResponseEntity<Void> createCourseLike(@Parameter(hidden = true)
                                          @UserId final Long userId,
                                          @PathVariable final Long courseId);

    @Operation(
            summary = "코스 좋아요 해제 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "코스 좋아요 해제 API",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 값이 올바르지 않습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "405",
                            description = "잘못된 HTTP method 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content)})
    ResponseEntity<Void> deleteCourseLike(@Parameter(hidden = true)
                                          @UserId final Long userId,
                                          @PathVariable final Long courseId);

    @Operation(
            summary = "코스 삭제 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "코스 삭제 API",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰의 값이 올바르지 않습니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰이 만료되었습니다. 재발급 받아주세요.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "405",
                            description = "잘못된 HTTP method 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content)})
    ResponseEntity<Void> deleteCourse(@Parameter(hidden = true)
                                      @UserId Long userId,
                                      @PathVariable final Long courseId);
}

