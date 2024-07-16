package org.dateroad.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.InvalidValueException;
import org.dateroad.tag.domain.DateTagType;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidatorUtil {

    //태그 리스트 사이즈 검증
    public static void validateUserTagSize(final List<DateTagType> userTags) {
        if (userTags.isEmpty() || userTags.size() > 3) {
            throw new InvalidValueException((FailureCode.WRONG_USER_TAG_SIZE));
        }
    }

}
