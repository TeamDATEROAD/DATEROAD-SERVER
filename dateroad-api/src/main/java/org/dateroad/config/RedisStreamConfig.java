package org.dateroad.config;


import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.CommandArgs;
import io.lettuce.core.protocol.CommandKeyword;
import io.lettuce.core.protocol.CommandType;
import lombok.RequiredArgsConstructor;
import org.dateroad.point.event.FreeEventListener;
import org.dateroad.point.event.PointEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import java.time.Duration;
import java.util.Iterator;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class RedisStreamConfig {
    private final PointEventListener pointEventListener;
    private final FreeEventListener freeEventListener;
    private final RedisTemplate<String, Object> redisTemplate;

    public void createStreamConsumerGroup(final String streamKey, final String consumerGroupName) {
        if (Boolean.FALSE.equals(redisTemplate.hasKey(streamKey))) {
            // Stream이 존재하지 않을 경우, MKSTREAM 옵션으로 스트림과 그룹을 생성
            redisTemplate.execute((RedisCallback<Void>) connection -> {
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
                redisTemplate.opsForStream().createGroup(streamKey, ReadOffset.from("0"), consumerGroupName);
            }
        }
    }

    // ConsumerGroup 존재 여부 확인
    public boolean isStreamConsumerGroupExist(final String streamKey, final String consumerGroupName) {
        Iterator<StreamInfo.XInfoGroup> iterator = redisTemplate
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
        createStreamConsumerGroup("coursePoint", "courseGroup");
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> containerOptions = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder().pollTimeout(Duration.ofMillis(100)).build();

        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container = StreamMessageListenerContainer.create(
                redisConnectionFactoryForCluster,
                containerOptions);

        Subscription subscription = container.receiveAutoAck(Consumer.from("courseGroup", "instance-1"),
                StreamOffset.create("coursePoint", ReadOffset.lastConsumed()), pointEventListener);
        container.start();
        return subscription;
    }

    @Bean
    public Subscription freeSubscription(RedisConnectionFactory redisConnectionFactoryForCluster) {
        createStreamConsumerGroup("courseFree", "courseGroup");
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> containerOptions = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder().pollTimeout(Duration.ofMillis(100)).build();
        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container = StreamMessageListenerContainer.create(
                redisConnectionFactoryForCluster,
                containerOptions);
        Subscription subscription = container.receiveAutoAck(Consumer.from("courseGroup", "instance-2"),
                StreamOffset.create("courseFree", ReadOffset.lastConsumed()), freeEventListener);
        container.start();
        return subscription;
    }
}
