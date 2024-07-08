package org.dateroad.users.api;

import lombok.RequiredArgsConstructor;
import org.dateroad.users.dto.request.UserSignUpReq;
import org.dateroad.users.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.dateroad.common.Constants.AUTHORIZATION;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/vi/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestHeader(AUTHORIZATION) final String token,
                                       @RequestBody final UserSignUpReq userSignUPReq) {

        userService.signUp(token, userSignUPReq);

        return ResponseEntity.ok().build();

    }


}
