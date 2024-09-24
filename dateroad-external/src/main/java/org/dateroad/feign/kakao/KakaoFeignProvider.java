package org.dateroad.feign.kakao;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.UnauthorizedException;
import org.dateroad.feign.kakao.dto.response.KakaoErrorRes;
import org.dateroad.feign.kakao.dto.response.KakaoAccessTokenInfoRes;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class KakaoFeignProvider {
    private final KakaoProperties kakaoProperties;
    private final KakaoFeignApi kakaoFeignApi;
    private final ObjectMapper objectMapper;

    private static final  String TARGETIDTYPE = "user_id";

    //AuthService에서 호출 : 카카오에서 주는 userId 받아오기
    public String getKakaoPlatformUserId(final String kakaoAccessToken) {
        String kakaoRequestHeader = getKakaoRequestHeader(KakaoRequestType.ACCESS_TOKEN_INFO, kakaoAccessToken);
        KakaoAccessTokenInfoRes kakaoAccessTokenInfoRes = getKakaoAccessTokenInfoFeign(kakaoRequestHeader);
        return String.valueOf(kakaoAccessTokenInfoRes.id());
    }

    //AuthService에서 호출 : 회원탈퇴 할 때, 카카오톡과 연결 끊기
    public void unLinkWithKakao(final String kakaoPlatformUserId) {
        String kakaoRequestHeader = getKakaoRequestHeader(KakaoRequestType.UN_LINK, null);
        try {
            kakaoFeignApi.unlink(kakaoRequestHeader, TARGETIDTYPE, Long.valueOf(kakaoPlatformUserId));
        } catch (FeignException e) {
            throw new UnauthorizedException(FailureCode.UN_LINK_WITH_KAKAO_UNAUTHORIZED);
        }
    }

    private KakaoAccessTokenInfoRes getKakaoAccessTokenInfoFeign(final String accessTokenWithTokenType) {
        try {
            return kakaoFeignApi.getKakaoPlatformUserId(accessTokenWithTokenType);
        } catch (FeignException e) {
            log.error("Kakao feign exception : ", e);

            //kakaoResponseDTO로 변경
            KakaoErrorRes errorResponse = convertToKakaoErrorResponse(e.contentUTF8());

            //카카오에서 주는 에러 코드가 -1이면 카카오 내부 에러, 나머지는 카카오 액세스 토큰 에러
            if (errorResponse.code() == -1) {
                log.error("Kakao feign exception : ", e);
                throw new UnauthorizedException(FailureCode.KAKAO_INTERNER_ERROR);
            } else {
                log.error("feign exception : ", e);
                throw new UnauthorizedException(FailureCode.INVALID_KAKAO_TOKEN);
            }
        }
    }

    private String getKakaoRequestHeader(final KakaoRequestType kakaoRequestType, final String accessToken) {
        if (kakaoRequestType == KakaoRequestType.ACCESS_TOKEN_INFO) {
            return KakaoHeaderType.BEARER.getTokenType() + accessToken;
        } else if (kakaoRequestType == KakaoRequestType.UN_LINK) {
            return KakaoHeaderType.KAKAOAK.getTokenType() + kakaoProperties.getAdminKey();
        } else {
            throw new UnauthorizedException(FailureCode.INVALID_KAKAO_ACCESS);
        }
    }

    private KakaoErrorRes convertToKakaoErrorResponse(String responseBody) {
        try {
            return objectMapper.readValue(responseBody, KakaoErrorRes.class);
        } catch (IOException e) {
            log.error("Convert To KakaoErrorResponse Error : ", e);
            throw new UnauthorizedException(FailureCode.INVALID_KAKAO_TOKEN);
        }
    }
}
