package org.dateroad.user.api;

import lombok.RequiredArgsConstructor;
import org.dateroad.auth.argumentresolve.UserId;
import org.dateroad.user.dto.request.UserSignInReq;
import org.dateroad.user.dto.request.UserSignUpReq;
import org.dateroad.user.dto.response.UserSignInRes;
import org.dateroad.user.dto.response.UsersignUpRes;
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
}
