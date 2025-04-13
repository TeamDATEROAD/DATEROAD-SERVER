package org.dateroad.point.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record PointGetAllRes(
        PointsDto gained,
        PointsDto used,
        Integer totalPoint
) {
    public static PointGetAllRes of(PointsDto gained, PointsDto used,Integer totalPoint) {
        return PointGetAllRes.builder()
                .gained(gained)
                .used(used)
                .totalPoint(totalPoint)
                .build();
    }
    @Builder(access = AccessLevel.PRIVATE)
    public record PointsDto(
            List<PointDtoRes> points
    ) {
        public static PointsDto of(List<PointDtoRes> points) {
            return PointsDto.builder()
                    .points(points)
                    .build();
        }
    }

    @Builder(access = AccessLevel.PRIVATE)
    public record PointDtoRes(
            int point,
            String description,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
            LocalDate createdAt
    ){
        public static PointDtoRes of(PointDto pointDto) {
            return PointDtoRes.builder()
                    .point(pointDto.point())
                    .description(pointDto.description())
                    .createdAt(pointDto.createdAt())
                    .build();
        }
    }
}
