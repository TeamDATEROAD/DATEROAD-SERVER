package org.dateroad.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactoryForCluster() {
        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration()
                .clusterNode("127.0.0.1", 7000)
                .clusterNode("127.0.0.1", 7001)
                .clusterNode("127.0.0.1", 7002)
                .clusterNode("127.0.0.1", 7003)
                .clusterNode("127.0.0.1", 7004)
                .clusterNode("127.0.0.1", 7005);
        // 클러스터 모드로 LettuceConnectionFactory 설정
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(clusterConfig);
        lettuceConnectionFactory.setShareNativeConnection(false); // 클러스터 모드에서 필요에 따라 사용할 수 있음
        return lettuceConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setConnectionFactory(redisConnectionFactoryForCluster());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

}
