package org.dateroad.users.api;

import lombok.RequiredArgsConstructor;
import org.dateroad.users.dto.request.UserSignUpReq;
import org.dateroad.users.dto.response.UsersignUpRes;
import org.dateroad.users.service.AuthService;
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
}
