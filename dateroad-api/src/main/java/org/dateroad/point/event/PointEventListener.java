package org.dateroad.point.event;

import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.DateRoadException;
import org.dateroad.point.domain.TransactionType;
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

    @Override
    @Transactional
    public void onMessage(final MapRecord<String, String, String> message) {
        Map<String, String> map = message.getValue();
        Long userId = Long.valueOf(map.get("userId"));
        TransactionType type = TransactionType.valueOf(map.get("type"));
        User user = getUser(userId);
        int point = Integer.parseInt(map.get("point"));
        switch (type) {
            case POINT_GAINED:
                user.setTotalPoint(user.getTotalPoint() + point);
                break;
            case POINT_USED:
                user.setTotalPoint(user.getTotalPoint() - point);
                break;
            default:
                throw new IllegalArgumentException("잘못된 TransactionType: " + type);
        }
        userRepository.save(user);
    }


    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new DateRoadException(FailureCode.USER_NOT_FOUND)
        );
    }
}
