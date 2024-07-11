package org.dateroad.course.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import org.dateroad.point.domain.TransactionType;

@Builder(access = AccessLevel.PROTECTED)
public record PointUseReq(
        int point,
        TransactionType type,
        String description
) {
    public static PointUseReq of(int point, TransactionType type, String description) {
        return PointUseReq.builder()
                .point(point)
                .type(type)
                .description(description)
                .build();
    }
}
