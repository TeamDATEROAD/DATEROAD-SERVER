package org.dateroad.point.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.EntityNotFoundException;
import org.dateroad.point.domain.Point;
import org.dateroad.point.domain.TransactionType;
import org.dateroad.point.dto.response.PointGetAllRes;
import org.dateroad.point.dto.response.PointGetAllRes.PointDtoRes;
import org.dateroad.point.dto.response.PointGetAllRes.PointsDto;
import org.dateroad.point.dto.response.PointDto;
import org.dateroad.point.repository.PointRepository;
import org.dateroad.user.domain.User;
import org.dateroad.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;
    private final UserRepository userRepository;

    private static final int ADS_POINT = 50;
    private static final String ADS_DESCRIPTION = "광고 시청";

    public PointGetAllRes getAllPoints(Long userId) {
        List<PointDto> points = pointRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(PointDto::of)
                .toList();
        PointsDto gainedPoints = pointTypeCheckToList(points, TransactionType.POINT_GAINED);
        PointsDto usedPoints = pointTypeCheckToList(points, TransactionType.POINT_USED);
        return PointGetAllRes.of(gainedPoints,usedPoints);
    }

    @Transactional
    public void awardAdsPoints(final Long userId) {
        User user = getUser(userId);
        user.setTotalPoint(user.getTotalPoint() + ADS_POINT);
        userRepository.save(user);
        pointRepository.save(Point.create(user, ADS_POINT, TransactionType.POINT_GAINED, ADS_DESCRIPTION));
    }

    private PointsDto pointTypeCheckToList(List<PointDto> points, TransactionType type){
        return PointsDto.of(points.stream()
                .filter(point -> point.transactionType() == type)
                .map(PointDtoRes::of)
                .toList());
    }

    private User getUser(final Long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND));
    }
}
