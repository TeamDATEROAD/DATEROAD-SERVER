package org.dateroad.common;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.date.domain.Course;
import org.dateroad.exception.DateRoadException;
import org.dateroad.exception.ForbiddenException;
import org.dateroad.exception.InvalidValueException;
import org.dateroad.exception.UnauthorizedException;
import org.dateroad.tag.domain.DateTagType;

import java.time.LocalDateTime;
import java.util.List;
import org.dateroad.user.domain.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidatorUtil {

    //태그 리스트 사이즈 검증
    public static <T> void validateTagSize(final List<T> tags, FailureCode code) {
        if (tags.isEmpty() || tags.size() > 3) {
            throw new InvalidValueException((code));
        }
    }

    //refreshToken 재발급할 때 검증
    public static void validateRefreshToken(LocalDateTime expireDate) {
        if (expireDate.isBefore(LocalDateTime.now())) {
            throw new UnauthorizedException(FailureCode.EXPIRED_REFRESH_TOKEN);
        }
    }

    public static void validateUserAndCourse(User user, Course course) {
        if (course.getUser().equals(user)) {
            throw new ForbiddenException(FailureCode.FORBIDDEN);
        }
    }

    public static <T> void validateListSizeMin(final List<T> list, int minSize, FailureCode code) {
        if (list.isEmpty() || list.size() < minSize) {
            throw new InvalidValueException(code);
        }
    }

    public static <T> void validateListSizeMax(final List<T> list, int maxSize, FailureCode code) {
        if (list.isEmpty() || list.size() > maxSize) {
            throw new InvalidValueException(code);
        }
    }

    public static void validStringMinSize(final String string, int minSize, FailureCode code) {
        if (string.length() < minSize) {
            throw new InvalidValueException(code);
        }
    }

    public static void validateDateNotInFuture(LocalDate date, FailureCode code) {
        if (date.isAfter(LocalDate.now())) {
            throw new InvalidValueException(code);
        }
    }
}
