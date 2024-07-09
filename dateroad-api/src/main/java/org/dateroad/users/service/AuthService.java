package org.dateroad.users.service;

import lombok.RequiredArgsConstructor;
import org.dateroad.auth.jwt.JwtProvider;
import org.dateroad.auth.jwt.Token;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.ConflictException;
import org.dateroad.exception.InvalidValueException;
import org.dateroad.feign.apple.ApplePlatformUserIdProvider;
import org.dateroad.feign.kakao.KakaoPlatformUserIdProvider;
import org.dateroad.tag.domain.DateTagType;
import org.dateroad.tag.domain.UserTag;
import org.dateroad.tag.repository.UserTagRepository;
import org.dateroad.user.domain.Platform;
import org.dateroad.user.domain.User;
import org.dateroad.user.repository.UserRepository;
import org.dateroad.users.dto.request.UserSignUpReq;
import org.dateroad.users.dto.response.UsersignUpRes;
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

    @Transactional
    public UsersignUpRes signUp(final String token, final UserSignUpReq userSignUpReq) {
        String platformUserId = getUserPlatformId(userSignUpReq.platform(), token);
        validateUserTagSize(userSignUpReq.tag());
        validateDuplicatedUser(userSignUpReq.platform(), platformUserId);
        User newUser = saveUser(userSignUpReq.name(), userSignUpReq.image(), userSignUpReq.platform(), platformUserId);
        saveUserTag(newUser, userSignUpReq.tag());
        Token issuedToken = jwtProvider.issueToken(newUser.getId());

        return UsersignUpRes.of(newUser.getId(), issuedToken.accessToken(), issuedToken.refreshToken());
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

    private void validateUserTagSize(final List<DateTagType> userTags) {
        if (userTags.isEmpty() || userTags.size() > 3) {
            throw new InvalidValueException((FailureCode.WRONG_USER_TAG_SIZE));
        }
    }
}
