package org.dateroad.user.api;

import lombok.RequiredArgsConstructor;
import org.dateroad.auth.argumentresolve.UserId;
import org.dateroad.user.dto.request.UserSignInReq;
import org.dateroad.user.dto.request.UserSignUpReq;
import org.dateroad.user.dto.response.UserJwtInfoRes;
import org.dateroad.user.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.dateroad.common.Constants.AUTHORIZATION;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final AuthService authService;

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

    @PatchMapping("/reissue")
    public ResponseEntity<UserJwtInfoRes> reissue(@RequestHeader(AUTHORIZATION) String refreshToken) {
        UserJwtInfoRes userJwtInfoRes = authService.reissue(refreshToken);
        return ResponseEntity
                .ok(userJwtInfoRes);
        
    }
}
