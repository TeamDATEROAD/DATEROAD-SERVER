package org.dateroad.point.service;

import org.dateroad.code.FailureCode;
import org.dateroad.exception.EntityNotFoundException;
import org.dateroad.user.domain.Platform;
import org.dateroad.user.domain.User;
import org.dateroad.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PointServiceTest {

    @Autowired
    private PointService pointService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testCreatePointForAds_increasesUserPointByAdsPoint() {
        int resetPoint = 100;
        int ADS_POINT = 50;

        // Given: 초기 포인트 100인 User를 생성하여 저장합니다.
        User user = User.create("Test User1", "testPlatformUserId", Platform.KAKAO, "testImageUrl");
        user.setTotalPoint(resetPoint);
        User savedUser = userRepository.save(user);
        Long userId = savedUser.getId();

        // When: PointService의 awardAdsPoints 메서드를 실행합니다.
        pointService.awardAdsPoints(userId);

        // Then: 데이터베이스에서 업데이트된 User를 조회하여 포인트가 50 증가했는지 확인합니다.
        User createPointForAdsUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND));

        assertEquals(resetPoint + ADS_POINT, createPointForAdsUser.getTotalPoint());
    }
}