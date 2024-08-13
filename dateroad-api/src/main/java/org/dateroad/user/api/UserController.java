package org.dateroad.user.api;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.dateroad.auth.argumentresolve.UserId;
import org.dateroad.tag.domain.DateTagType;
import org.dateroad.user.dto.request.AppleWithdrawAuthCodeReq;
import org.dateroad.user.dto.request.UserSignInReq;
import org.dateroad.user.dto.request.UserSignUpReq;
import org.dateroad.user.dto.response.UserInfoGetMyPageRes;
import org.dateroad.user.dto.response.UserJwtInfoRes;
import org.dateroad.user.dto.response.UserInfoMainRes;
import org.dateroad.user.service.AuthService;
import org.dateroad.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.dateroad.common.Constants.AUTHORIZATION;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController implements UserApi {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserJwtInfoRes> signUp(@RequestHeader(AUTHORIZATION) final String token,
                                                 @RequestPart("userSignUpReq") final UserSignUpReq userSignUPReq,
                                                 @Nullable @RequestPart("image") MultipartFile image,
                                                 @RequestPart("tag") List<DateTagType> tag
    ) {
        UserJwtInfoRes userSignUpRes = authService.signUp(token, userSignUPReq, image, tag);
        return ResponseEntity.status(HttpStatus.CREATED).body(userSignUpRes);
    }

    @PostMapping("/signin")
    public ResponseEntity<UserJwtInfoRes> signIn(@RequestHeader(AUTHORIZATION) final String token,
                                                @RequestBody final UserSignInReq userSignInReq) {
        UserJwtInfoRes userSignInRes = authService.signIn(token, userSignInReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(userSignInRes);
    }

    @DeleteMapping("/signout")
    public ResponseEntity<Void> signout(@UserId final Long userId) {
        authService.signout(userId);
		return ResponseEntity.ok().build();
	}

    @GetMapping("/check")
    public ResponseEntity<Void> checkNickname(@RequestParam("name") final String nickname) {
        authService.checkNickname(nickname);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@UserId final Long userId,
                                         @RequestBody final AppleWithdrawAuthCodeReq appleWithdrawAuthCodeReq) {
        authService.withdraw(userId, appleWithdrawAuthCodeReq);
        return ResponseEntity.ok().build();
	}
    @PatchMapping("/reissue")
    public ResponseEntity<UserJwtInfoRes> reissue(@RequestHeader(AUTHORIZATION) final String refreshToken) {
        UserJwtInfoRes userJwtInfoRes = authService.reissue(refreshToken);
        return ResponseEntity.ok(userJwtInfoRes);
    }

    @GetMapping("/main")
    public ResponseEntity<UserInfoMainRes> getUserInfo(@UserId final Long userId) {
        UserInfoMainRes userInfoMainRes = userService.getUserInfoMain(userId);
        return ResponseEntity.ok(userInfoMainRes);
    }

    @GetMapping
    public ResponseEntity<UserInfoGetMyPageRes> getUserInfoMyPage(@UserId final Long userId) {
        UserInfoGetMyPageRes userInfoGetMyPageRes = userService.getUserInfoMyPage(userId);
        return ResponseEntity.ok(userInfoGetMyPageRes);
    }

    @PatchMapping
    public ResponseEntity<Void> patchUserProfile(@UserId final Long userId,
                                                 @RequestPart("name") final String name,
                                                 @RequestPart("tags") final List<DateTagType> tags,
                                                 @Nullable @RequestPart(name = "image", required = false) final MultipartFile image ) throws IOException, ExecutionException, InterruptedException {
        userService.editUserProfile(userId, name, tags, image);
        return ResponseEntity
                .ok()
                .build();
    }
}
