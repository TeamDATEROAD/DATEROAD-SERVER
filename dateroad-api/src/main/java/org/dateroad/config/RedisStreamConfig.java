package org.dateroad.config;


import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.CommandArgs;
import io.lettuce.core.protocol.CommandKeyword;
import io.lettuce.core.protocol.CommandType;
import lombok.RequiredArgsConstructor;
import org.dateroad.date.repository.CourseRepository;
import org.dateroad.point.event.FreeEventListener;
import org.dateroad.point.event.pointEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import java.time.Duration;
import java.util.Iterator;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class RedisStreamConfig {
    private final pointEventListener pointEventListener;
    private final FreeEventListener freeEventListener;
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    public void createStreamConsumerGroup(final String streamKey, final String consumerGroupName) {
        // Stream이 존재 하지 않으면, MKSTREAM 옵션을 통해 만들고, ConsumerGroup또한 생성한다
        System.out.println(streamKey + consumerGroupName);
        // Stream이 존재하지 않으면, MKSTREAM 옵션을 통해 스트림과 소비자 그룹을 생성
        if (Boolean.FALSE.equals(redisTemplate().hasKey(streamKey))) {
            RedisAsyncCommands<String, String> commands = (RedisAsyncCommands<String, String>) Objects.requireNonNull(
                            redisTemplate()
                                    .getConnectionFactory())
                    .getConnection()
                    .getNativeConnection();

            CommandArgs<String, String> args = new CommandArgs<>(StringCodec.UTF8)
                    .add(CommandKeyword.CREATE)
                    .add(streamKey)
                    .add(consumerGroupName)
                    .add("0")
                    .add("MKSTREAM");
            // MKSTREAM 옵션을 사용하여 스트림과 그룹을 생성
            commands.dispatch(CommandType.XGROUP, new StatusOutput<>(StringCodec.UTF8), args).toCompletableFuture()
                    .join();
        }
        // Stream 존재 시, ConsumerGroup 존재 여부 확인 후 ConsumerGroup을 생성
        else {
            if (!isStreamConsumerGroupExist(streamKey, consumerGroupName)) {
                redisTemplate().opsForStream().createGroup(streamKey, ReadOffset.from("0"), consumerGroupName);
            }
        }
    }

    // ConsumerGroup 존재 여부 확인
    public boolean isStreamConsumerGroupExist(final String streamKey, final String consumerGroupName) {
        Iterator<StreamInfo.XInfoGroup> iterator = redisTemplate()
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
    public Subscription PointSubscription() {
        createStreamConsumerGroup("coursePoint", "courseGroup");
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> containerOptions = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder().pollTimeout(Duration.ofMillis(100)).build();

        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container = StreamMessageListenerContainer.create(
                redisConnectionFactory(),
                containerOptions);

        Subscription subscription = container.receiveAutoAck(Consumer.from("courseGroup", "instance-1"),
                StreamOffset.create("coursePoint", ReadOffset.lastConsumed()), pointEventListener);
        container.start();
        return subscription;
    }

    @Bean
    public Subscription FreeSubscription() {
        createStreamConsumerGroup("courseFree", "courseGroup");
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> containerOptions = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder().pollTimeout(Duration.ofMillis(100)).build();
        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container = StreamMessageListenerContainer.create(
                redisConnectionFactory(),
                containerOptions);
        Subscription subscription = container.receiveAutoAck(Consumer.from("courseGroup", "instance-2"),
                StreamOffset.create("courseFree", ReadOffset.lastConsumed()), freeEventListener);
        container.start();
        return subscription;
    }
}

