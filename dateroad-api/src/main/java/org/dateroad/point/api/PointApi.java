package org.dateroad.point.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dateroad.auth.argumentresolve.UserId;
import org.dateroad.point.dto.response.PointGetAllRes;
import org.springframework.http.ResponseEntity;

@Tag(name = "포인트 관련 API")
@SecurityRequirement(name = "Authorization")
public interface PointApi {
    @Operation(
            summary = "포인트 내역 조회 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = PointGetAllRes.class),
                                    examples = @ExampleObject(value = """
                                        {
                                            "gained": {
                                                "points": [
                                                    {
                                                        "point": 100,
                                                        "description": "첫 구매 적립",
                                                        "createdAt": "2023.07.14"
                                                    },
                                                    {
                                                        "point": 200,
                                                        "description": "리뷰 작성 적립",
                                                        "createdAt": "2023.07.15"
                                                    }
                                                ]
                                            },
                                            "used": {
                                                "points": [
                                                    {
                                                        "point": 50,
                                                        "description": "구매 사용",
                                                        "createdAt": "2023.07.16"
                                                    },
                                                    {
                                                        "point": 80,
                                                        "description": "이벤트 참여 사용",
                                                        "createdAt": "2023.07.17"
                                                    }
                                                ]
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
    public ResponseEntity<PointGetAllRes> getAllPoints(
            @Parameter(hidden = true)
            @UserId Long userId
    );
}
