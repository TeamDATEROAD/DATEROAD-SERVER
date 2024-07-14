package org.dateroad.advertisment.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dateroad.advertisment.dto.response.AdvGetAllRes;
import org.dateroad.advertisment.dto.response.AdvGetDetailRes;
import org.dateroad.auth.argumentresolve.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "광고 관련 API")
@SecurityRequirement(name = "Authorization")
public interface AdvertismentApi {
    @Operation(
            summary = "광고 전체 조회 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = AdvGetAllRes.class),
                                    examples = @ExampleObject(value = """
                                        {
                                            "advertismentDtoResList": [
                                                {
                                                    "advertismentId": 1,
                                                    "thumbnail": "http://example.com/thumbnail1.jpg",
                                                    "title": "광고 제목 1",
                                                },
                                                {
                                                    "advertismentId": 2,
                                                    "thumbnail": "http://example.com/thumbnail2.jpg",
                                                    "title": "광고 제목 2",
                                                },
                                                {
                                                    "advertismentId": 3,
                                                    "thumbnail": "http://example.com/thumbnail2.jpg",
                                                    "title": "광고 제목 3",
                                                },
                                                {
                                                    "advertismentId": 4,
                                                    "thumbnail": "http://example.com/thumbnail2.jpg",
                                                    "title": "광고 제목 4",
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
    ResponseEntity<AdvGetAllRes> getAllAdvertisments();

    @Operation(
            summary = "광고 상세 조회 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = AdvGetDetailRes.class),
                                    examples = @ExampleObject(value = """
                                        {
                                            "images": [
                                                {
                                                    "imagesUrl": "http://example.com/image1.jpg",
                                                    "sequence": 1
                                                },
                                                {
                                                    "imagesUrl": "http://example.com/image2.jpg",
                                                    "sequence": 2
                                                }
                                            ],
                                            "title": "광고 제목",
                                            "createAt": "2023.07.14",
                                            "description": "광고 설명"
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
                            responseCode = "400",
                            description = "유효하지 않은 요청 파라미터 값입니다.",
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
                            responseCode = "404",
                            description = "존재하지 않는 광고입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "405",
                            description = "잘못된 HTTP method 요청입니다.",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 내부 오류입니다.",
                            content = @Content)})
    ResponseEntity<AdvGetDetailRes> getAdvertismentsDetail(
            @Parameter(required = true) final @PathVariable Long advId
    );
}
