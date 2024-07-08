package org.dateroad.users.api;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.dateroad.users.dto.request.UserSignUpReq;
import org.dateroad.users.dto.response.UsersignUpRes;
import org.dateroad.users.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.dateroad.common.Constants.AUTHORIZATION;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UsersignUpRes> signUp(@RequestHeader(AUTHORIZATION) final String token,
                                                @RequestBody final UserSignUpReq userSignUPReq) {
        UsersignUpRes userSignUpRes = userService.signUp(token, userSignUPReq);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userSignUpRes);
    }

    @PostMapping("/hi")
    public ResponseEntity<Void> test() {
        return ResponseEntity.ok().build();
    }
}
