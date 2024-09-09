package org.dateroad.config;



import lombok.RequiredArgsConstructor;
import org.dateroad.point.event.FreeEventListener;
import org.dateroad.point.event.PointEventListener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer.StreamMessageListenerContainerOptions;
import org.springframework.data.redis.stream.Subscription;

import java.time.Duration;
import java.util.Iterator;

@Configuration
@RequiredArgsConstructor
public class RedisStreamConfig {
    private final PointEventListener pointEventListener;
    private final FreeEventListener freeEventListener;
    private final RedisTemplate<String, String> redistemplateForCluster;

    public void createStreamConsumerGroup(final String streamKey, final String consumerGroupName) {
        if (Boolean.FALSE.equals(redistemplateForCluster.hasKey(streamKey))) {
            // Stream이 존재하지 않을 경우, MKSTREAM 옵션으로 스트림과 그룹을 생성
            redistemplateForCluster.execute((RedisCallback<Void>) connection -> {
                if (connection.isPipelined() || connection.isQueueing()) {
                    throw new UnsupportedOperationException("Pipelined or queued connections are not supported for cluster.");
                }
                byte[] streamKeyBytes = streamKey.getBytes();
                byte[] consumerGroupNameBytes = consumerGroupName.getBytes();

                if (connection instanceof RedisClusterConnection clusterConnection) {
                    // 클러스터 모드에서 명령 실행
                    clusterConnection.execute("XGROUP", "CREATE".getBytes(), streamKeyBytes, consumerGroupNameBytes, "0".getBytes(), "MKSTREAM".getBytes());
                } else {
                    // 비클러스터 모드에서 명령 실행
                    connection.execute("XGROUP", "CREATE".getBytes(), streamKeyBytes, consumerGroupNameBytes, "0".getBytes(), "MKSTREAM".getBytes());
                }
                return null;
            });
        } else {
            // Stream이 존재할 경우 ConsumerGroup 존재 여부 확인 후 생성
            if (!isStreamConsumerGroupExist(streamKey, consumerGroupName)) {
                redistemplateForCluster.opsForStream().createGroup(streamKey, ReadOffset.from("0"), consumerGroupName);
            }
        }
    }

    public boolean isStreamConsumerGroupExist(final String streamKey, final String consumerGroupName) {
        Iterator<StreamInfo.XInfoGroup> iterator = redistemplateForCluster
                .opsForStream().groups(streamKey).stream().iterator();
        while (iterator.hasNext()) {
            StreamInfo.XInfoGroup xInfoGroup = iterator.next();
            if (xInfoGroup.groupName().equals(consumerGroupName)) {
                return true;
            }
        }
        return false;
    }

    @Bean
    public Subscription pointSubscription(RedisConnectionFactory redisConnectionFactoryForCluster) {
        createStreamConsumerGroup("coursePoint", "coursePointGroup");
        StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> containerOptions = StreamMessageListenerContainerOptions
                .builder().pollTimeout(Duration.ofMillis(100)).build();
        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container = StreamMessageListenerContainer.create(
                redisConnectionFactoryForCluster, containerOptions);
        Subscription subscription = container.receiveAutoAck(Consumer.from("coursePointGroup", "instance-1"),
                StreamOffset.create("coursePoint", ReadOffset.lastConsumed()), pointEventListener);
        container.start();
        return subscription;
    }

    @Bean
    public Subscription freeSubscription(RedisConnectionFactory redisConnectionFactoryForCluster) {
        createStreamConsumerGroup("courseFree", "courseFreeGroup");
        StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> containerOptions = StreamMessageListenerContainerOptions
                .builder().pollTimeout(Duration.ofMillis(100))
                .build();

        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container = StreamMessageListenerContainer.create(
                redisConnectionFactoryForCluster,
                containerOptions);
        Subscription subscription = container.receiveAutoAck(Consumer.from("courseFreeGroup", "instance-2"),
                StreamOffset.create("courseFree", ReadOffset.lastConsumed()), freeEventListener);
        container.start();
        return subscription;
    }
}

