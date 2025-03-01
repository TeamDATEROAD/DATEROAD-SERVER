package org.dateroad.user.facade;

import io.micrometer.common.lang.Nullable;
import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.common.Constants;
import org.dateroad.config.RedisLockManager;
import org.dateroad.exception.DateRoadException;
import org.dateroad.tag.domain.DateTagType;
import org.dateroad.user.dto.request.AppleWithdrawAuthCodeReq;
import org.dateroad.user.dto.request.UserSignInReq;
import org.dateroad.user.dto.request.UserSignUpReq;
import org.dateroad.user.dto.response.UserJwtInfoRes;
import org.dateroad.user.service.AuthService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Component
public class AuthFacade {
    private final RedisLockManager redisLockManager;
    private final AuthService authService;

    public UserJwtInfoRes lettuceSignUp(final String token, final UserSignUpReq userSignUpReq, @Nullable final MultipartFile image, final List<DateTagType> tag) {
        boolean lockAcquired = redisLockManager.acquireLock(token, Constants.SIGN_UP_LOCK_TYPE, 10L);

        if (!lockAcquired) {
            throw new DateRoadException(FailureCode.REDIS_LOCK_ERROR);
        } try {
            return authService.signUp(token, userSignUpReq, image, tag);
        } finally {
            redisLockManager.releaseLock(token);
        }
    }

    public UserJwtInfoRes signIn(final String token, final UserSignInReq userSignInReq) {
        return authService.signIn(token, userSignInReq);
    }

    public UserJwtInfoRes reissue(final String refreshToken) {
        return authService.reissue(refreshToken);
    }

    public void withdraw(final Long userId, final AppleWithdrawAuthCodeReq appleWithdrawAuthCodeReq) {
        authService.withdraw(userId, appleWithdrawAuthCodeReq);
    }

    public void signOut(final long userId) {
        authService.signOut(userId);
    }

    public void checkNickname(final String nickname) {
        authService.checkNickname(nickname);
    }
}
