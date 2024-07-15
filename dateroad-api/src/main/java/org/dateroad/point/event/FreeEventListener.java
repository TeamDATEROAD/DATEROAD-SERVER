package org.dateroad.point.event;

import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.EntityNotFoundException;
import org.dateroad.user.domain.User;
import org.dateroad.user.repository.UserRepository;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class FreeEventListener implements StreamListener<String, MapRecord<String, String, String>> {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void onMessage(final MapRecord<String, String, String> message) {
        Map<String, String> map = message.getValue();
        Long userId = Long.valueOf(map.get("userId"));
        User user = getUser(userId);
        int userPoint = user.getFree();
        user.setFree(userPoint -1);
        userRepository.save(user);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(FailureCode.USER_NOT_FOUND)
        );
    }
}
