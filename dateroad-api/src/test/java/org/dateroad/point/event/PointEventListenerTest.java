package org.dateroad.point.event;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import org.dateroad.exception.DateRoadException;
import org.dateroad.point.domain.Point;
import org.dateroad.point.repository.PointRepository;
import org.dateroad.user.domain.User;
import org.dateroad.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.stream.MapRecord;

@ExtendWith(MockitoExtension.class)
class PointEventListenerTest {

    @Mock
    private UserService userService;

    @Mock
    private PointRepository pointRepository;

    @InjectMocks
    private PointEventListener pointEventListener;

    @Test
    void 이벤트를_받으면_User의_포인트증가와_메소드가실행되었는지_검사한다() throws DateRoadException {
        // Given
        Map<String, String> messageMap = Map.of(
                "userId", "39",
                "point", "100",
                "type", "POINT_GAINED",
                "description", "Course creation"
        );

        MapRecord<String, String, String> record = MapRecord.create("coursePoint", messageMap);

        User user = mock(User.class);
        when(userService.getUser(39L)).thenReturn(user);
        when(user.getTotalPoint()).thenReturn(100);

        // When
        pointEventListener.onMessage(record);

        // Then
        verify(userService).getUser(39L);
        verify(user).setTotalPoint(200); // Assuming POINT_GAINED adds the point
        verify(pointRepository,times(1)).save(any(Point.class));
        verify(userService).saveUser(user);
    }
}