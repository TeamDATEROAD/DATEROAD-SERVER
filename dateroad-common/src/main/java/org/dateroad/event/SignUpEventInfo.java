package org.dateroad.event;
import org.dateroad.code.EventCode;

public record SignUpEventInfo (
        EventCode eventCode,
        String nickName,
        int count,
        String signUpPlatform
) {
    public static SignUpEventInfo of(EventCode eventCode, String nickName, int count, String signUpPlatform) {
        return new SignUpEventInfo(eventCode, nickName, count, signUpPlatform);
    }
}
