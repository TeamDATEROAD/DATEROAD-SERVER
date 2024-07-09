package org.dateroad.user.service;

import lombok.RequiredArgsConstructor;
import org.dateroad.auth.jwt.JwtProvider;
import org.dateroad.auth.jwt.Token;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.ConflictException;
import org.dateroad.exception.EntityNotFoundException;
import org.dateroad.exception.InvalidValueException;
import org.dateroad.feign.apple.ApplePlatformUserIdProvider;
import org.dateroad.feign.kakao.KakaoPlatformUserIdProvider;
import org.dateroad.refreshtoken.repository.RefreshTokenRepository;
import org.dateroad.tag.domain.DateTagType;
import org.dateroad.tag.domain.UserTag;
import org.dateroad.tag.repository.UserTagRepository;
import org.dateroad.user.domain.Platform;
import org.dateroad.user.domain.User;
import org.dateroad.user.dto.request.UserSignInReq;
import org.dateroad.user.repository.UserRepository;
import org.dateroad.user.dto.request.UserSignUpReq;
import org.dateroad.user.dto.response.UserSignInRes;
import org.dateroad.user.dto.response.UsersignUpRes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final KakaoPlatformUserIdProvider kakaoPlatformUserIdProvider;
    private final ApplePlatformUserIdProvider applePlatformUserIdProvider;
    private final UserTagRepository userTagRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

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
        User foundUser = getUser(userSignInReq.platform(), platformUserId);
        Token issuedToken = jwtProvider.issueToken(foundUser.getId());
        return UserSignInRes.of(foundUser.getId(), issuedToken.accessToken(), issuedToken.refreshToken());
    }


    public void checkNickname(final String nickname) {
        if (!userRepository.existsByName(nickname)) {
            return;
        } else {
            throw new ConflictException(FailureCode.DUPLICATE_NICKNAME);
        }
    }

    private String getUserPlatformId(final Platform platform, final String token) {
        if (platform == Platform.APPLE) {
            return applePlatformUserIdProvider.getApplePlatformUserId(token);
        } else if (platform == Platform.KAKAO) {
            return kakaoPlatformUserIdProvider.getKakaoPlatformUserId(token);
        } else {
            throw new InvalidValueException(FailureCode.INVALID_PLATFORM_TYPE);
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

    //유저 가져오기(검색)
    private User getUser(final Platform platform, final String platformUserId) {
        return userRepository.findUserByPlatFormAndPlatformUserId(platform, platformUserId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND));
    }

    private void validateUserTagSize(final List<DateTagType> userTags) {
        if (userTags.isEmpty() || userTags.size() > 3) {
            throw new InvalidValueException((FailureCode.WRONG_USER_TAG_SIZE));
        }
    }
}