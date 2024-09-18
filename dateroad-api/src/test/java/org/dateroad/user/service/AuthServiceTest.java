package org.dateroad.user.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.dateroad.auth.jwt.JwtProvider;
import org.dateroad.auth.jwt.Token;
import org.dateroad.code.FailureCode;
import org.dateroad.event.SignUpEventInfo;
import org.dateroad.exception.ConflictException;
import org.dateroad.feign.apple.AppleFeignProvider;
import org.dateroad.feign.discord.DiscordFeignProvider;
import org.dateroad.feign.kakao.KakaoFeignProvider;
import org.dateroad.image.service.ImageService;
import org.dateroad.refreshtoken.repository.RefreshTokenRepository;
import org.dateroad.tag.domain.DateTagType;
import org.dateroad.tag.domain.UserTag;
import org.dateroad.tag.repository.UserTagRepository;
import org.dateroad.user.domain.Platform;
import org.dateroad.user.domain.User;
import org.dateroad.user.dto.request.UserSignUpReq;
import org.dateroad.user.dto.response.UserJwtInfoRes;
import org.dateroad.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private KakaoFeignProvider kakaoFeignProvider;

    @Mock
    private AppleFeignProvider appleFeignProvider;

    @Mock
    private UserTagRepository userTagRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private DiscordFeignProvider discordFeignProvider;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    // 회원가입 테스트
    @Test
    @DisplayName("회원가입 성공 테스트")
    void testSignUp_Success() {
        // given
        String token = "testToken";
        MultipartFile image = null;  // 이미지 업로드 테스트는 생략
        List<DateTagType> tags = List.of(DateTagType.DRIVE); // 태그 리스트
        String platformUserId = "platformUserId";
        String nickname = "성준";

        UserSignUpReq userSignUpReq = new UserSignUpReq(nickname, Platform.KAKAO); // 회원가입 요청 객체

        when(userRepository.existsByName(nickname)).thenReturn(false); // 닉네임 중복 체크
        when(kakaoFeignProvider.getKakaoPlatformUserId(token)).thenReturn(platformUserId); // 카카오 플랫폼 유저 ID 조회
        when(userRepository.existsUserByPlatFormAndPlatformUserId(Platform.KAKAO, platformUserId)).thenReturn(false); // 중복 유저 확인
        when(imageService.getImageUrl(image)).thenReturn("imageUrl"); // 이미지 처리
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            // 유저 ID를 수동으로 설정 (테스트 환경에서는 DB가 없으므로)
            user.setId(1L);
            return user;
        });// 유저 저장
        when(jwtProvider.issueToken(anyLong())).thenReturn(new Token("accessToken", "refreshToken")); // 토큰 발급
        // when
        UserJwtInfoRes result = authService.signUp(token, userSignUpReq, image, tags);

        // then
        assertThat(result).isNotNull();
        assertThat(result.accessToken()).isEqualTo("accessToken");
        assertThat(result.refreshToken()).isEqualTo("refreshToken");

        // Discord webhook 전송 확인
        verify(discordFeignProvider).sendSignUpInfoToDiscord(any(SignUpEventInfo.class));

        // 유저 저장 여부 확인
        verify(userRepository).save(any(User.class));

        // 태그 저장 여부 확인
        verify(userTagRepository, times(tags.size())).save(any(UserTag.class));
    }

    // 닉네임 중복으로 인한 회원가입 실패 테스트
    @Test
    @DisplayName("중복 닉네임으로 회원가입 시 ConflictException 발생")
    void testSignUp_DuplicateNickname() {
        // given
        String token = "testToken";
        MultipartFile image = null;
        List<DateTagType> tags = List.of(DateTagType.DRIVE);
        String nickname = "성준";

        UserSignUpReq userSignUpReq = new UserSignUpReq(nickname, Platform.KAKAO);

        when(userRepository.existsByName(nickname)).thenReturn(true); // 닉네임이 이미 존재한다고 설정

        // when & then
        assertThatThrownBy(() -> authService.signUp(token, userSignUpReq, image, tags))
                .isInstanceOf(ConflictException.class)
                .hasMessage(FailureCode.DUPLICATE_NICKNAME.getMessage());

        // 유저 저장 및 태그 저장 호출되지 않았는지 검증
        verify(userRepository, never()).save(any(User.class));
        verify(userTagRepository, never()).save(any(UserTag.class));
    }
}
