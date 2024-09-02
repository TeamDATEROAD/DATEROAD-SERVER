package org.dateroad.course.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dateroad.point.domain.TransactionType;

@Getter
@Builder(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PointUseReq {
    @Builder.Default
    @Min(0)
    private final int point = 100;
    @Builder.Default
    private final TransactionType type = TransactionType.POINT_GAINED;
    @Builder.Default
    private final String description = "코스 열람하기";

    public static PointUseReq of(final int point, final TransactionType type, final String description) {
        return PointUseReq.builder()
                .point(point)
                .type(type)
                .description(description)
                .build();
    }
}
