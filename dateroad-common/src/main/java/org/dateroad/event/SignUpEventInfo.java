package org.dateroad.event;
import lombok.Builder;
import org.dateroad.code.EventCode;

@Builder
public record SignUpEventInfo (
        EventCode eventCode,
        String nickName,
        int count,
        String signUpPlatform
) {
    public static SignUpEventInfo of(final EventCode eventCode, final String nickName, final int count, final String signUpPlatform) {
        return SignUpEventInfo.builder()
                .eventCode(eventCode)
                .nickName(nickName)
                .count(count)
                .signUpPlatform(signUpPlatform)
                .build();
    }
}
