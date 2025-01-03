package org.dateroad.user.service;

import io.micrometer.common.lang.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dateroad.auth.jwt.JwtProvider;
import org.dateroad.auth.jwt.Token;
import org.dateroad.code.EventCode;
import org.dateroad.code.FailureCode;
import org.dateroad.event.SignUpEventInfo;
import org.dateroad.exception.*;
import org.dateroad.feign.apple.AppleFeignProvider;
import org.dateroad.feign.discord.DiscordFeignProvider;
import org.dateroad.feign.kakao.KakaoFeignProvider;
import org.dateroad.exception.ConflictException;
import org.dateroad.exception.EntityNotFoundException;
import org.dateroad.exception.UnauthorizedException;
import org.dateroad.image.service.ImageService;
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
import static org.dateroad.common.ValidatorUtil.validateTagSize;


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
    private final PointRepository pointRepository;
    private final DiscordFeignProvider discordFeignProvider;
    private final ImageService imageService;

    @Transactional
    public UserJwtInfoRes signUp(final String token, final UserSignUpReq userSignUpReq, @Nullable final MultipartFile image, final List<DateTagType> tag) {
        String platformUserId = getUserPlatformId(userSignUpReq.platform(), token);
        validateTagSize(tag,FailureCode.WRONG_USER_TAG_SIZE);
        checkNickname(userSignUpReq.name());
        validateDuplicatedUser(userSignUpReq.platform(), platformUserId);
        User newUser = saveUser(userSignUpReq.name(), imageService.getImageUrl(image), userSignUpReq.platform(), platformUserId);
        saveUserTag(newUser, tag);
        Token issuedToken = issueToken(newUser.getId());

        sendSignUpEventToDiscord(userSignUpReq);
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
    public void withdraw(final Long userId, final AppleWithdrawAuthCodeReq appleWithdrawAuthCodeReq) {
        User foundUser = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        if (foundUser.getPlatForm() == Platform.KAKAO) {
            kakaoFeignProvider.unLinkWithKakao(foundUser.getPlatformUserId());
        } else if (foundUser.getPlatForm() == Platform.APPLE) {
            appleFeignProvider.revokeUser(appleWithdrawAuthCodeReq.authCode());
        } else {
            throw new InvalidValueException(FailureCode.INVALID_PLATFORM_TYPE);
        }
        deleteAllDataByUser(foundUser);
    }

    @Transactional
    public void signOut(final long userId) {
        User foundUser = getUserByUserId(userId);
        deleteRefreshToken(foundUser.getId());
    }

    public void checkNickname(final String nickname) {
        if (userRepository.existsByName(nickname)) {
            throw new ConflictException(FailureCode.DUPLICATE_NICKNAME);
        }
    }

    private void sendSignUpEventToDiscord(final UserSignUpReq userSignUpReq) {
        int userCount = (int) userRepository.countByNameNot("삭제된유저");

        discordFeignProvider.sendSignUpInfoToDiscord(SignUpEventInfo.of(
                EventCode.DISCORD_SIGNUP_EVENT,
                userSignUpReq.name(),
                userCount,
                String.valueOf(userSignUpReq.platform())));
    }

    private String getUserPlatformId(final Platform platform, final String token) {
        if (platform == Platform.APPLE) {
            return appleFeignProvider.getApplePlatformUserId(token);
        } else if (platform == Platform.KAKAO) {
            return kakaoFeignProvider.getKakaoPlatformUserId(token);
        } else {
            throw new BadRequestException(FailureCode.INVALID_PLATFORM_TYPE);
        }
    }

    private void validateDuplicatedUser(final Platform platform, final String platformUserId) {
        if (userRepository.existsUserByPlatFormAndPlatformUserId(platform, platformUserId)) {
            throw new ConflictException(FailureCode.DUPLICATE_USER);
        }
    }

    private User saveUser(final String name, final String image, final Platform platform, final String platformUserId) {
        User user = User.create(name, platformUserId, platform, image);
        return userRepository.save(user);
    }

    private void saveUserTag(final User savedUser, final List<DateTagType> userTags) {
        userTags.stream()
                .map(dateTagType -> UserTag.create(savedUser, dateTagType))
                .forEach(userTagRepository::save);
    }

    private User getUserByPlatformAndPlatformUserId(final Platform platform, final String platformUserId) {
        return userRepository.findUserByPlatFormAndPlatformUserId(platform, platformUserId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND));
    }

    private User getUserByUserId(final long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND));
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

    private Token issueToken(final Long userId) {
        return jwtProvider.issueToken(userId);
    }

    private void deleteRefreshToken(final Long userId) {
        refreshTokenRepository.deleteRefreshTokenByUserId(userId);
    }

    //todo: 추후에 유저 탈퇴 시, 삭제 정보 정해지면 삭제 추가 구현
    //유저 탈퇴 시, 유저 관련 데이터만 삭제
    protected void deleteAllDataByUser(final User user) {

        //유저태그 삭제
        userTagRepository.deleteAllByUserId(user.getId());
//        user.setDeleted(true);

        //유저포인트 삭제
        pointRepository.deleteAllByUserId(user.getId());

        //유저데이트 삭제
//        List<Date> foundDatesByUser = dateRepository.findAllByUser(user);
//        foundDatesByUser.forEach(dates -> dates.setDeleted(true));
//        dateRepository.deleteAllByUserId(userId);

        //유저 date_access 삭제
//        dateAccessRepository.deleteAllByUserId(user.getId());

        //유저 코스 삭제
//        courseRepository.deleteAllByUserId(userId);
//        List<Course> foundCoursesByUser = courseRepository.findAllByUser(user);
//        foundCoursesByUser.forEach(course -> course.setDeleted(true));

        //유저 좋아요 삭제
//        likeRepository.deleteAllByUserId(user.getId());

        //리프레시토큰 삭제
        deleteRefreshToken(user.getId());

        user.setPlatformUserId("USER DELETED");
        user.setName("삭제된유저");
        //유저 삭제
//        userRepository.deleteById(userId);
//        user.setDeleted(true);
    }
}
