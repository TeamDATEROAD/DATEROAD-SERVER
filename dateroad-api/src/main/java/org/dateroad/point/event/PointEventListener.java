package org.dateroad.point.event;

import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.course.dto.request.PointUseReq;
import org.dateroad.exception.EntityNotFoundException;
import org.dateroad.exception.UnauthorizedException;
import org.dateroad.point.domain.Point;
import org.dateroad.point.domain.TransactionType;
import org.dateroad.point.repository.PointRepository;
import org.dateroad.user.domain.User;
import org.dateroad.user.repository.UserRepository;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PointEventListener implements StreamListener<String, MapRecord<String, String, String>> {
    private final UserRepository userRepository;
    private final PointRepository pointRepository;

    @Override
    @Transactional
    public void onMessage(final MapRecord<String, String, String> message) {
        Map<String, String> map = message.getValue();
        Long userId = Long.valueOf(map.get("userId"));
        TransactionType type = TransactionType.valueOf(map.get("type"));
        User user = getUser(userId);
        int point = Integer.parseInt(map.get("point"));
        String description = map.get("description");
        switch (type) {
            case POINT_GAINED:
                user.setTotalPoint(user.getTotalPoint() + point);
                break;
            case POINT_USED:
                user.setTotalPoint(user.getTotalPoint() - point);
                break;
            default:
                throw new UnauthorizedException(FailureCode.INVALID_TRANSACTION_TYPE);
        }
        pointRepository.save(Point.create(user,point,type,description));
        userRepository.save(user);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND)
        );
    }
}
