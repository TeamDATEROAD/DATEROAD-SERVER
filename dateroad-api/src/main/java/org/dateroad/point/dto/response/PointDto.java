package org.dateroad.point.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.Builder;
import org.dateroad.point.domain.Point;
import org.dateroad.point.domain.TransactionType;

@Builder
public record PointDto(
        int point,
        String description,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate createdAt,
        TransactionType transactionType

){
    public static PointDto of(Point point) {
        return PointDto.builder()
                .point(point.getPoint())
                .description(point.getDescription())
                .createdAt(point.getCreatedAt().toLocalDate())
                .transactionType(point.getTransactionType())
                .build();
    }
}