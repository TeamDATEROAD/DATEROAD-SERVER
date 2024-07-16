package org.dateroad.user.api;

import static org.dateroad.common.Constants.AUTHORIZATION;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.dateroad.auth.argumentresolve.UserId;
import org.dateroad.tag.domain.DateTagType;
import org.dateroad.user.dto.request.AppleWithdrawAuthCodeReq;
import org.dateroad.user.dto.request.UserSignInReq;
import org.dateroad.user.dto.request.UserSignUpReq;
import org.dateroad.user.dto.response.UserInfoGetMyPageRes;
import org.dateroad.user.dto.response.UserInfoMainRes;
import org.dateroad.user.dto.response.UserJwtInfoRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "유저 관련 API")
@SecurityRequirement(name = "Authorization")
public interface UserApi {


    @Operation(summary = "회원 가입", description = "사용자가 회원가입을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserJwtInfoRes.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    ResponseEntity<UserJwtInfoRes> signUp(
            @RequestHeader(name = "Authorization", required = true) @Parameter(description = "Bearer 토큰") final String token,
            @RequestPart(name = "userSignUpReq", required = true) @Parameter(description = "회원가입 요청 데이터") final UserSignUpReq userSignUpReq,
            @RequestPart(name = "image", required = true) @Parameter(description = "프로필 이미지") MultipartFile image,
            @RequestPart(name = "tag", required = true) @Parameter(description = "사용자 태그") List<DateTagType> tag
    ) throws IOException;

    @Operation(summary = "로그인", description = "사용자가 로그인을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserJwtInfoRes.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    ResponseEntity<UserJwtInfoRes> signIn(
            @RequestHeader(name = "Authorization", required = true) @Parameter(description = "Bearer 토큰") final String token,
            @RequestBody(description = "로그인 요청 데이터", required = true) final UserSignInReq userSignInReq
    );

    @Operation(summary = "로그아웃", description = "사용자가 로그아웃을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    ResponseEntity<Void> signout(
            @Parameter(description = "사용자 ID", required = true) @UserId final Long userId
    );

    @Operation(summary = "닉네임 중복 체크", description = "닉네임 중복을 체크합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    ResponseEntity<Void> checkNickname(
            @RequestParam(name = "name", required = true) @Parameter(description = "닉네임") final String nickname
    );

    @Operation(summary = "회원 탈퇴", description = "사용자가 회원 탈퇴를 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    ResponseEntity<Void> withdraw(
            @Parameter(description = "사용자 ID", required = true) @UserId final Long userId,
            @RequestBody(description = "애플 탈퇴 인증 코드 요청 데이터", required = true) final AppleWithdrawAuthCodeReq appleWithdrawAuthCodeReq
    );

    @Operation(summary = "토큰 재발급", description = "리프레시 토큰으로 새로운 토큰을 재발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserJwtInfoRes.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    ResponseEntity<UserJwtInfoRes> reissue(
            @RequestHeader(name = "Authorization", required = true) @Parameter(description = "리프레시 토큰") final String refreshToken
    );

    @Operation(summary = "유저 프로필 조회 (MAIN)", description = "사용자의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserInfoMainRes.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    ResponseEntity<UserInfoMainRes> getUserInfo(
            @Parameter(description = "사용자 ID", required = true) @UserId final Long userId
    );

    @Operation(summary = "내 프로필 조회 API", description = "마이페이지의 사용자의 정보를 조회.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserInfoGetMyPageRes.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    ResponseEntity<UserInfoGetMyPageRes> getUserInfoMyPage(@UserId final Long userId);

    @Operation(summary = "내 프로필 정보 수정", description = "마이페이지의 사용자의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    ResponseEntity<Void> patchUserProfile(@UserId final Long userId,
                                          @RequestPart("name") final String name,
                                          @RequestPart("tags") final List<DateTagType> tags,
                                          @RequestPart("image") final MultipartFile image)
            throws IOException, ExecutionException, InterruptedException;

}
