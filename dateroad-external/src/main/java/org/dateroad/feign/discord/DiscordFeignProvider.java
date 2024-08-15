package org.dateroad.feign.discord;

import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dateroad.code.EventCode;
import org.dateroad.code.FailureCode;
import org.dateroad.event.SignUpEventInfo;
import org.dateroad.exception.BadRequestException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class DiscordFeignProvider {
    private final DiscordFeignApi discordFeignApi;

    //디스코드 연동 메서드
    public void sendSignUpInfoToDiscord(final SignUpEventInfo signUpEventInfo) {
        EventCode eventCode = signUpEventInfo.eventCode();
        String discordMessage = String.format(
                eventCode.getEventMessage(),
                signUpEventInfo.nickName(),
                signUpEventInfo.count(),
                signUpEventInfo.signUpPlatform()
        );

        //discord 양식에 맞게 변경4
        DiscordFeignReq discordFeignReq = DiscordFeignReq.of(discordMessage);
        doDiscordOpenFeign(discordFeignReq);
    }

    //feign통신
    private void doDiscordOpenFeign(final DiscordFeignReq discordFeignReq) {
        try {
            discordFeignApi.sendMessage(discordFeignReq);
        } catch (FeignException e) {
            throw new BadRequestException(FailureCode.INVALID_DISCORD_SIGNUP_MESSAGE);
        }
    }
}
