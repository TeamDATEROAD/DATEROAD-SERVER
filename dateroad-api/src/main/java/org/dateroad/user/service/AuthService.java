package org.dateroad.user.service;

import lombok.RequiredArgsConstructor;
import org.dateroad.auth.jwt.JwtProvider;
import org.dateroad.auth.jwt.Token;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.*;
import org.dateroad.feign.apple.AppleFeignProvider;
import org.dateroad.feign.kakao.KakaoFeignApi;
import org.dateroad.feign.kakao.KakaoFeignProvider;
import org.dateroad.refreshtoken.repository.RefreshTokenRepository;
import org.dateroad.tag.domain.DateTagType;
import org.dateroad.tag.domain.UserTag;
import org.dateroad.tag.repository.UserTagRepository;
import org.dateroad.user.domain.Platform;
import org.dateroad.user.domain.User;
import org.dateroad.user.dto.request.AppleWithdrawAuthCodeReq;
import org.dateroad.user.dto.request.UserSignInReq;
import org.dateroad.user.repository.UserRepository;
import org.dateroad.user.dto.request.UserSignUpReq;
import org.dateroad.user.dto.response.UserSignInRes;
import org.dateroad.user.dto.response.UsersignUpRes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final KakaoFeignProvider kakaoFeignProvider;
    private final AppleFeignProvider appleFeignProvider;
    private final UserTagRepository userTagRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final KakaoFeignApi kakaoFeignApi;

    @Transactional
    public UsersignUpRes signUp(final String token, final UserSignUpReq userSignUpReq) {
        String platformUserId = getUserPlatformId(userSignUpReq.platform(), token);
        validateUserTagSize(userSignUpReq.tag());
        checkNickname(userSignUpReq.name());
        validateDuplicatedUser(userSignUpReq.platform(), platformUserId);
        User newUser = saveUser(userSignUpReq.name(), userSignUpReq.image(), userSignUpReq.platform(), platformUserId);
        saveUserTag(newUser, userSignUpReq.tag());
        Token issuedToken = jwtProvider.issueToken(newUser.getId());

        return UsersignUpRes.of(newUser.getId(), issuedToken.accessToken(), issuedToken.refreshToken());
    }

    @Transactional
    public UserSignInRes signIn(final String token, final UserSignInReq userSignInReq) {
        String platformUserId = getUserPlatformId(userSignInReq.platform(), token);
        User foundUser = getUserByPlatformAndPlatformUserId(userSignInReq.platform(), platformUserId);
        Token issuedToken = jwtProvider.issueToken(foundUser.getId());
        return UserSignInRes.of(foundUser.getId(), issuedToken.accessToken(), issuedToken.refreshToken());
    }

    @Transactional
    public void withdraw(final Long userId, final AppleWithdrawAuthCodeReq AppleWithdrawAuthCodeReq) {

        //todo: #45브랜치 머지후, 메서드 이용
        User foundUser = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        if (foundUser.getPlatForm() == Platform.KAKAO) {    //카카오 유저면 카카오와 연결 끊기
            kakaoFeignProvider.unLinkWithKakao(foundUser.getPlatformUserId());
        } else if (foundUser.getPlatForm() == Platform.APPLE) {    //애플 유저면 애플이랑 연결 끊기
            appleFeignProvider.revokeUser(AppleWithdrawAuthCodeReq.authCode());
        } else {
            throw new BadRequestException(FailureCode.INVALID_PLATFORM_TYPE);
        }

        //todo: #45브랜치 머지후, 메서드 이용
        refreshTokenRepository.deleteByUserId(foundUser.getId());
        userRepository.deleteById(foundUser.getId());
    }

    //닉네임 중복체크
    public void checkNickname(final String nickname) {
        if (!userRepository.existsByName(nickname)) {
            return;
        } else {
            throw new ConflictException(FailureCode.DUPLICATE_NICKNAME);
        }
	}

    @Transactional
    public void signout(final long userId) {
        User foundUser = getUserByUserId(userId);
        deleteRefreshToken(foundUser.getId());
    }

    //플랫폼 유저 아이디 가져오기 (카카오 or 애플)
    private String getUserPlatformId(final Platform platform, final String token) {
        if (platform == Platform.APPLE) {
            return appleFeignProvider.getApplePlatformUserId(token);
        } else if (platform == Platform.KAKAO) {
            return kakaoFeignProvider.getKakaoPlatformUserId(token);
        } else {
            throw new BadRequestException(FailureCode.INVALID_PLATFORM_TYPE);
        }
    }

    //중복유저 검증 메서드
    private void validateDuplicatedUser(final Platform platform, final String platformUserId) {
        if (userRepository.existsUserByPlatFormAndPlatformUserId(platform, platformUserId)) {
            throw new ConflictException(FailureCode.DUPLICATE_USER);
        }
    }

    //유저 생성
    private User saveUser(final String name, final String image, final Platform platform, final String platformUserId) {
        User user = User.create(name, platformUserId, platform, image);
        return userRepository.save(user);
    }

    //유저 태그 생성
    private void saveUserTag(final User savedUser, final List<DateTagType> userTags) {
        List<UserTag> userTageList = userTags.stream()
                .map(dateTagType -> UserTag.create(savedUser, dateTagType))
                .map(userTagRepository::save)
                .toList();
    }

    //유저 가져오기(platform이랑 platformUserId 사용)
    private User getUserByPlatformAndPlatformUserId(final Platform platform, final String platformUserId) {
        return userRepository.findUserByPlatFormAndPlatformUserId(platform, platformUserId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND)
                );
    }

    //유저 가져오기(userId 사용)
    private User getUserByUserId(final long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND)
        );
    }

    //태그 리스트 사이즈 검증
    private void validateUserTagSize(final List<DateTagType> userTags) {
        if (userTags.isEmpty() || userTags.size() > 3) {
            throw new BadRequestException((FailureCode.WRONG_USER_TAG_SIZE));
        }
    }

    //refreshToken 삭제
    private void deleteRefreshToken(final long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
