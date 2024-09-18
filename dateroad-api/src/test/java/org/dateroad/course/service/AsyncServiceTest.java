package org.dateroad.course.service;


import java.util.HashMap;
import java.util.Map;
import org.dateroad.course.dto.request.PointUseReq;
import org.dateroad.course.service.AsyncService;
import org.dateroad.point.domain.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsyncServiceTest {

    @Mock
    private RedisTemplate<String, String> redistemplateForCluster;
    @Mock
    private StreamOperations<String, Object, Object> streamOperations;
    @InjectMocks
    private AsyncService asyncService;

    @Test
    void 포인트_이벤트를_보내면_Stream에_다음키값으로_다음메소드가_호출과_객체가_들어갔는지_검사() {
        //given
        Long userId = 1L;
        PointUseReq pointUseReq = PointUseReq.of(100, TransactionType.POINT_GAINED,"point create");
        when(redistemplateForCluster.opsForStream()).thenReturn(streamOperations);
        //when
        asyncService.publishEvenUserPoint(userId, pointUseReq);
        //then
        Map<String,String> expectedFieldMap = new HashMap<>();
        expectedFieldMap.put("userId", userId.toString());
        expectedFieldMap.put("point", String.valueOf(pointUseReq.getPoint()));
        expectedFieldMap.put("type", pointUseReq.getType().name());
        expectedFieldMap.put("description", pointUseReq.getDescription());
        verify(redistemplateForCluster, times(1)).opsForStream();
        verify(streamOperations).add("coursePoint", expectedFieldMap);
    }

    @Test
    void 무료_이벤트를_보내면_Stream에_다음키값으로_다음메소드가_호출과_객체가_들어갔는지_검사() {
        //given
        Long userId = 1L;
        when(redistemplateForCluster.opsForStream()).thenReturn(streamOperations);
        //when
        asyncService.publishEventUserFree(userId);
        Map<String,String> expectedFieldMap = new HashMap<>();
        expectedFieldMap.put("userId", userId.toString());
        //then
        verify(redistemplateForCluster, times(1)).opsForStream();
        verify(streamOperations).add("courseFree", expectedFieldMap);
    }
}