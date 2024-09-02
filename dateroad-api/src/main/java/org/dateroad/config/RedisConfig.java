package org.dateroad.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${aws.ip}")
    private String host;

    @Bean
    public RedisConnectionFactory redisConnectionFactoryForCluster() {
        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration()
                .clusterNode(host, 7000)
                .clusterNode(host, 7001)
                .clusterNode(host, 7002)
                .clusterNode(host, 7003)
                .clusterNode(host, 7004)
                .clusterNode(host, 7005);
        // 클러스터 모드로 LettuceConnectionFactory 설정
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(clusterConfig);
        lettuceConnectionFactory.setShareNativeConnection(false); // 클러스터 모드에서 필요에 따라 사용할 수 있음
        return lettuceConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setConnectionFactory(redisConnectionFactoryForCluster());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

}
