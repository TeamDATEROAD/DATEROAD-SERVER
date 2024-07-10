package org.dateroad.user.api;

import lombok.RequiredArgsConstructor;
import org.dateroad.auth.argumentresolve.UserId;
import org.dateroad.user.dto.request.AppleWithdrawAuthCodeReq;
import org.dateroad.user.dto.request.UserSignInReq;
import org.dateroad.user.dto.request.UserSignUpReq;
import org.dateroad.user.dto.response.UserInfoMainRes;
import org.dateroad.user.dto.response.UserSignInRes;
import org.dateroad.user.dto.response.UsersignUpRes;
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
    public ResponseEntity<UsersignUpRes> signUp(@RequestHeader(AUTHORIZATION) final String token,
                                                @RequestBody final UserSignUpReq userSignUPReq) {
        UsersignUpRes userSignUpRes = authService.signUp(token, userSignUPReq);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userSignUpRes);
    }

    @PostMapping("/signin")
    public ResponseEntity<UserSignInRes> signIn(@RequestHeader(AUTHORIZATION) final String token,
                                                @RequestBody final UserSignInReq userSignInReq) {
        UserSignInRes userSignInRes = authService.signIn(token, userSignInReq);
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

    @GetMapping("/main")
    public ResponseEntity<UserInfoMainRes> getUserInfo(@UserId final Long userId) {
        UserInfoMainRes userInfoMainRes = userService.getUserInfoMain(userId);
        return ResponseEntity
                .ok(userInfoMainRes);
    }
}
