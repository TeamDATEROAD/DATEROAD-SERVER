package org.dateroad.point.event;

import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.DateRoadException;
import org.dateroad.exception.UnauthorizedException;
import org.dateroad.point.domain.Point;
import org.dateroad.point.domain.TransactionType;
import org.dateroad.point.repository.PointRepository;
import org.dateroad.user.domain.User;
import org.dateroad.user.service.UserService;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class PointEventListener implements StreamListener<String, MapRecord<String,String,String>>{
    private final UserService userService;
    private final RedisTemplate<String, String> redisTemplateForCluster;
    private final PointRepository pointRepository;

    @Override
    @Transactional
    public void onMessage(final MapRecord<String, String, String> message) throws DateRoadException {
        try {
            String recordId = message.getId().getValue();
            Map<String, String> map = message.getValue();
            Long userId = Long.valueOf(map.get("userId"));
            TransactionType type = TransactionType.valueOf(map.get("type"));
            User user = userService.getUser(userId);
            int point = Integer.parseInt(map.get("point"));
            String description = map.get("description");
            int beforePoint = user.getTotalPoint();
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
            userService.saveUser(user);
            pointRepository.save(Point.create(user, point, type, description));
            redisTemplateForCluster.opsForStream().acknowledge("coursePoint", "coursePointGroup", recordId);
            log.info("Redis onMessage[POINT]:{}:{}:BEFORE:{} => AFTER:{}", user.getId(), type.getDescription(), beforePoint, user.getTotalPoint());
        } catch (RedisSystemException e) {
            log.error("Redis Listener Error: ERROR: {}", e.getMessage());
            throw new DateRoadException(FailureCode.POINT_CREATE_ERROR);
        } catch (Exception e) {
            log.error("General Listener Error: ERROR: {}", e.getMessage());
            throw new DateRoadException(FailureCode.POINT_CREATE_ERROR);
        }
    }
}