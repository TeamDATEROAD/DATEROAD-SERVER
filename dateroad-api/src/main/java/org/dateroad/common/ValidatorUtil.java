package org.dateroad.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.InvalidValueException;
import org.dateroad.exception.UnauthorizedException;
import org.dateroad.tag.domain.DateTagType;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidatorUtil {

    //태그 리스트 사이즈 검증
    public static void validateUserTagSize(final List<DateTagType> userTags) {
        if (userTags.isEmpty() || userTags.size() > 3) {
            throw new InvalidValueException((FailureCode.WRONG_USER_TAG_SIZE));
        }
    }

    //refreshToken 재발급할 때 검증
    public static void validateRefreshToken(LocalDateTime expireDate) {
        if (expireDate.isBefore(LocalDateTime.now())) {
            throw new UnauthorizedException(FailureCode.EXPIRED_REFRESH_TOKEN);
        }
    }
}
