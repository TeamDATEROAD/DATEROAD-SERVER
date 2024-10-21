package org.dateroad.course.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dateroad.code.FailureCode;
import org.dateroad.exception.DateRoadException;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.PendingMessage;
import org.springframework.data.redis.connection.stream.PendingMessages;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class CourseRollbackService {
    private final RedisTemplate<String, String> redistemplateForCluster;

    public void rollbackCourse(final RecordId recordId) {
        PendingMessages pendingMessage = redistemplateForCluster.opsForStream().pending(
                "coursePoint", Consumer.from("coursePointGroup", "instance-1"), Range.unbounded(), 100L
        );
        for (PendingMessage message : pendingMessage) {
            if(message.getId() == recordId){
                redistemplateForCluster.opsForStream().acknowledge("coursePoint", "coursePointGroup", recordId);
                throw new DateRoadException(FailureCode.COURSE_CREATE_ERROR);
            }
        }
    }
}
