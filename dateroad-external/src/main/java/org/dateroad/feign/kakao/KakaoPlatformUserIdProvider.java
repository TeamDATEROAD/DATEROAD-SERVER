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
public class KakaoPlatformUserIdProvider {
    private final KakaoFeignApi kakaoFeignApi;
    private final ObjectMapper objectMapper;
    private static final String TOKEN_TYPE = "Bearer ";

    //AuthService에서 호출 : 카카오에서 주는 userId 받아오기
    public String getKakaoPlatformUserId(final String accessToken) {
        String kakaoAccessTokenWithTokenType = getAccessTokenWithTokenType(accessToken);
        KakaoAccessTokenInfoRes kakaoAccessTokenInfoRes = getKakaoAccessTokenInfo(kakaoAccessTokenWithTokenType);
        return String.valueOf(kakaoAccessTokenInfoRes.id());
    }

    private KakaoAccessTokenInfoRes getKakaoAccessTokenInfo(final String token) {
        try {
            return kakaoFeignApi.getKakaoPlatformUserId(token);
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

    private String getAccessTokenWithTokenType(String accessToken) {
        return TOKEN_TYPE + accessToken;
    }

    private KakaoErrorRes convertToKakaoErrorResponse(String responseBody) {
        try {
            return objectMapper.readValue(responseBody, KakaoErrorRes.class);
        } catch (IOException e) {
            log.error("Convert To KakaoErrorResponse Error : ", e);
            throw new UnauthorizedException(FailureCode.UNAUTHORIZED);
        }
    }
}