package org.dateroad.point.event;

import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.DateRoadException;
import org.dateroad.exception.EntityNotFoundException;
import org.dateroad.user.domain.User;
import org.dateroad.user.repository.UserRepository;
import org.dateroad.user.service.UserService;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class FreeEventListener implements StreamListener<String, MapRecord<String, String, String>> {
    private final UserService userService;
    private final RedisTemplate<String, String> redistemplateForCluster;
    @Override
    @Transactional
    public void onMessage(final MapRecord<String, String, String> message) {
        try {
            String stream = message.getStream();
            String recordId = message.getId().getValue();
            Map<String, String> map = message.getValue();
            Long userId = Long.valueOf(map.get("userId"));
            User user = userService.getUser(userId);
            int userFree = user.getFree();
            user.setFree(userFree - 1);
            userService.saveUser(user);
            log.info("Redis onMessage[FREE]:{}:BEFORE:{} => AFTER:{}", user.getId(),userFree,user.getFree());
            redistemplateForCluster.opsForStream().acknowledge("courseFree", "courseFreeGroup", recordId);
        }catch (Exception e) {
            log.error("redis Listener Error:ERROR: {}", e.getMessage());
            throw new DateRoadException(FailureCode.POINT_CREATE_ERROR);
        }
    }
}
