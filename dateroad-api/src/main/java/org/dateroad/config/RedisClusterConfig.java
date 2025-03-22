package org.dateroad.config;

import io.lettuce.core.ReadFrom;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
@Profile("!local")
public class RedisClusterConfig {

    @Value("${aws.ip}")
    private String host;

    @Value("${spring.data.redis.cluster.password}")
    private String password;

    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactoryForCluster() {
        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration()
                .clusterNode(host, 7001)
                .clusterNode(host, 7002)
                .clusterNode(host, 7003)
                .clusterNode(host, 7004)
                .clusterNode(host, 7005)
                .clusterNode(host, 7006);
        clusterConfig.setPassword(RedisPassword.of(password));
        SocketOptions socketOptions = SocketOptions.builder()
                .connectTimeout(Duration.ofSeconds(5L))
                .tcpNoDelay(true)
                .keepAlive(true)
                .build();

        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions
                .builder()
                .dynamicRefreshSources(true)
                .enableAllAdaptiveRefreshTriggers()
                .enablePeriodicRefresh() // 60초마다 refresh
                .refreshTriggersReconnectAttempts(3) // 재연결 시도 후 갱신
                .build();

        ClusterClientOptions clusterClientOptions = ClusterClientOptions
                .builder()
                .socketOptions(socketOptions)
                .pingBeforeActivateConnection(true) // 연결 활성화 전에 ping
                .autoReconnect(true)
                .topologyRefreshOptions(clusterTopologyRefreshOptions)
                .validateClusterNodeMembership(false)
                .nodeFilter(it ->
                        ! (it.is(RedisClusterNode.NodeFlag.FAIL)
                                || it.is(RedisClusterNode.NodeFlag.EVENTUAL_FAIL)
                                || it.is(RedisClusterNode.NodeFlag.HANDSHAKE)
                                || it.is(RedisClusterNode.NodeFlag.NOADDR)))
                .maxRedirects(3).build();

        final LettuceClientConfiguration clientConfig = LettuceClientConfiguration
                .builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .commandTimeout(Duration.ofSeconds(5L)) // 명령 타임아웃 5초로 설정
                .clientOptions(clusterClientOptions)
                .build();

        clusterConfig.setMaxRedirects(3);
        LettuceConnectionFactory factory = new LettuceConnectionFactory(clusterConfig, clientConfig);
        factory.setValidateConnection(false);
        factory.setShareNativeConnection(true);

        return factory;
    }


    @Bean
    public RedisTemplate<String, String> redisTemplateForCluster() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactoryForCluster());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    @Primary
    public CacheManager cacheManagerToCluster() {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofMinutes(60L));

        return RedisCacheManager
                .RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactoryForCluster())
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }
}

