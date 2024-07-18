package org.dateroad.user.service;

import io.micrometer.common.lang.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dateroad.auth.jwt.JwtProvider;
import org.dateroad.auth.jwt.Token;
import org.dateroad.code.FailureCode;
import org.dateroad.date.repository.CourseRepository;
import org.dateroad.date.service.DateRepository;
import org.dateroad.dateAccess.repository.DateAccessRepository;
import org.dateroad.exception.*;
import org.dateroad.feign.apple.AppleFeignProvider;
import org.dateroad.feign.kakao.KakaoFeignProvider;
import org.dateroad.exception.ConflictException;
import org.dateroad.exception.EntityNotFoundException;
import org.dateroad.exception.UnauthorizedException;
import org.dateroad.like.repository.LikeRepository;
import org.dateroad.point.repository.PointRepository;
import org.dateroad.refreshtoken.domain.RefreshToken;
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
import org.dateroad.user.dto.response.UserJwtInfoRes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.dateroad.common.ValidatorUtil.validateRefreshToken;
import static org.dateroad.common.ValidatorUtil.validateUserTagSize;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final KakaoFeignProvider kakaoFeignProvider;
    private final AppleFeignProvider appleFeignProvider;
    private final UserTagRepository userTagRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final PointRepository pointRepository;
    private final DateRepository dateRepository;
    private final DateAccessRepository dateAccessRepository;
    private final CourseRepository courseRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public UserJwtInfoRes signUp(final String token, final UserSignUpReq userSignUpReq, @Nullable final MultipartFile image, final List<DateTagType> tag) {
        String platformUserId = getUserPlatformId(userSignUpReq.platform(), token);
        validateUserTagSize(tag);
        checkNickname(userSignUpReq.name());
        validateDuplicatedUser(userSignUpReq.platform(), platformUserId);
        User newUser = saveUser(userSignUpReq.name(), userService.getImageUrl(image), userSignUpReq.platform(), platformUserId);
        saveUserTag(newUser, tag);
        Token issuedToken = issueToken(newUser.getId());
        return UserJwtInfoRes.of(newUser.getId(), issuedToken.accessToken(), issuedToken.refreshToken());
    }

    @Transactional
    public UserJwtInfoRes signIn(final String token, final UserSignInReq userSignInReq) {
        String platformUserId = getUserPlatformId(userSignInReq.platform(), token);
        User foundUser = getUserByPlatformAndPlatformUserId(userSignInReq.platform(), platformUserId);
        deleteRefreshToken(foundUser.getId());
        Token issuedToken = issueToken(foundUser.getId());
        return UserJwtInfoRes.of(foundUser.getId(), issuedToken.accessToken(), issuedToken.refreshToken());
    }

    @Transactional
    public UserJwtInfoRes reissue(final String refreshToken) {
        RefreshToken foundRefreshToken = getRefreshTokenByToken(refreshToken);
        validateRefreshToken(foundRefreshToken.getExpiredAt());
        Long userId = foundRefreshToken.getUserId();
        deleteRefreshToken(userId);
        Token newToken = issueToken(userId);
        return UserJwtInfoRes.of(userId, newToken.accessToken(), newToken.refreshToken());
    }

    @Transactional
    public void withdraw(final Long userId, final AppleWithdrawAuthCodeReq AppleWithdrawAuthCodeReq) {

        User foundUser = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        if (foundUser.getPlatForm() == Platform.KAKAO) {    //카카오 유저면 카카오와 연결 끊기
            kakaoFeignProvider.unLinkWithKakao(foundUser.getPlatformUserId());
        } else if (foundUser.getPlatForm() == Platform.APPLE) {    //애플 유저면 애플이랑 연결 끊기
            appleFeignProvider.revokeUser(AppleWithdrawAuthCodeReq.authCode());
        } else {
            throw new InvalidValueException(FailureCode.INVALID_PLATFORM_TYPE);
        }

        deleteAllDataByUser(foundUser.getId());
    }

    //닉네임 중복체크
    public void checkNickname(final String nickname) {
        if (userRepository.existsByName(nickname)) {
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

    private RefreshToken getRefreshTokenByToken(final String refreshToken) {
        try {
            return refreshTokenRepository.findRefreshTokenByToken(refreshToken)
                    .orElseThrow(() -> new UnauthorizedException(FailureCode.UNAUTHORIZED));
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw new UnauthorizedException(FailureCode.INVALID_REFRESH_TOKEN_VALUE);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UnauthorizedException((FailureCode.UNAUTHORIZED));
        }
    }

    //토큰 발급
    private Token issueToken(final Long userId) {
        return jwtProvider.issueToken(userId);
    }

    //리프레시 토큰 삭제
    private void deleteRefreshToken(final Long userId) {
        refreshTokenRepository.deleteRefreshTokenByUserId(userId);
    }

    //유저 탈퇴 시, 모든 유저 정보 삭제
    private void deleteAllDataByUser(final Long userId) {

        //유저태그 삭제
        userTagRepository.deleteAllByUserId(userId);

        //유저포인트 삭제
        pointRepository.deleteAllByUserId(userId);

        //유저데이트 삭제
        dateRepository.deleteAllByUserId(userId);

        //유저 date_access 삭제
        dateAccessRepository.deleteAllByUserId(userId);

        //유저 코스 삭제
        courseRepository.deleteAllByUserId(userId);

        //유저 좋아요 삭제
        likeRepository.deleteAllByUserId(userId);

        //리프레시토큰 삭제
        deleteRefreshToken(userId);

        //유저 삭제
        userRepository.deleteById(userId);
    }
}
