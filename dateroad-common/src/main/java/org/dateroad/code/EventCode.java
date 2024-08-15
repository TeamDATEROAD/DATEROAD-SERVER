package org.dateroad.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EventCode {
    DISCORD_SIGNUP_EVENT(
            "[👩‍❤️‍👨 데이트로드 신규 유저 가입 👩‍❤️‍👨]\n1)닉네임: [%s]\n2)현재 가입자 수: [%d]\n3) 회원가입 플랫폼: [%s]\n"),
    ;

    private final String eventMessage;
}
