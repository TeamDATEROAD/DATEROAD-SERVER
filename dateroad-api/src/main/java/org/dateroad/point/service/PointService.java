package org.dateroad.point.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.dateroad.point.domain.TransactionType;
import org.dateroad.point.dto.response.PointGetAllRes;
import org.dateroad.point.dto.response.PointGetAllRes.PointDtoRes;
import org.dateroad.point.dto.response.PointGetAllRes.PointsDto;
import org.dateroad.point.dto.response.PointDto;
import org.dateroad.point.repository.PointRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;
            
    public PointGetAllRes getAllPoints(Long userId) {
        List<PointDto> points = pointRepository.findAllByUserId(userId)
                .stream().map(PointDto::of)
                .toList();

        PointsDto gainedPoints = PointsDto.of(points.stream()
                .filter(point -> point.transactionType() == TransactionType.POINT_GAINED)
                .map(PointDtoRes::of)
                .toList());

        PointsDto usedPoints = PointsDto.of(points.stream()
                .filter(point -> point.transactionType() == TransactionType.POINT_USED)
                .map(PointDtoRes::of)
                .toList());

        return PointGetAllRes.of(gainedPoints,usedPoints);
    }
}
