package org.dateroad.user.api;

import lombok.RequiredArgsConstructor;
import org.dateroad.auth.argumentresolve.UserId;
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

import static org.dateroad.common.Constants.AUTHORIZATION;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserJwtInfoRes> signUp(@RequestHeader(AUTHORIZATION) final String token,
                                                 @RequestBody final UserSignUpReq userSignUPReq) {
        UserJwtInfoRes userSignUpRes = authService.signUp(token, userSignUPReq);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userSignUpRes);
    }

    @PostMapping("/signin")
    public ResponseEntity<UserJwtInfoRes> signIn(@RequestHeader(AUTHORIZATION) final String token,
                                                @RequestBody final UserSignInReq userSignInReq) {
        UserJwtInfoRes userSignInRes = authService.signIn(token, userSignInReq);
        return ResponseEntity
                .ok(userSignInRes);
    }

    @DeleteMapping("/signout")
    public ResponseEntity<Void> signout(@UserId final Long userId) {
        authService.signout(userId);
		return ResponseEntity
			    .ok()
				.build();
	}

    @GetMapping("/check")
    public ResponseEntity<Void> checkNickname(@RequestParam("name") final String nickname) {
        authService.checkNickname(nickname);
        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@UserId final Long userId,
                                         @RequestBody final AppleWithdrawAuthCodeReq appleWithdrawAuthCodeReq) {
        authService.withdraw(userId, appleWithdrawAuthCodeReq);
        return ResponseEntity
                .ok()
                .build();
	}
    @PatchMapping("/reissue")
    public ResponseEntity<UserJwtInfoRes> reissue(@RequestHeader(AUTHORIZATION) final String refreshToken) {
        UserJwtInfoRes userJwtInfoRes = authService.reissue(refreshToken);
        return ResponseEntity
                .ok(userJwtInfoRes);

    }

    @GetMapping("/main")
    public ResponseEntity<UserInfoMainRes> getUserInfo(@UserId final Long userId) {
        UserInfoMainRes userInfoMainRes = userService.getUserInfoMain(userId);
        return ResponseEntity
                .ok(userInfoMainRes);
    }

    @GetMapping
    public ResponseEntity<UserInfoGetMyPageRes> getUserInfoMyPage(@UserId final Long userId) {
        UserInfoGetMyPageRes userInfoGetMyPageRes = userService.getUserInfoMyPage(userId);
        return ResponseEntity.ok(userInfoGetMyPageRes);
    }
}
