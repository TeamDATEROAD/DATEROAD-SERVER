package org.dateroad.course.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record DateAccessCreateRes(
        int userPoint,
        int userFreeRemained,
        Long userPurchaseCount
) {
    public static DateAccessCreateRes of(final int userPoint, final int userFreeRemained, final Long userPurchaseCount) {
        return DateAccessCreateRes.builder()
                .userPoint(userPoint)
                .userFreeRemained(userFreeRemained)
                .userPurchaseCount(userPurchaseCount)
                .build();
    }
}
