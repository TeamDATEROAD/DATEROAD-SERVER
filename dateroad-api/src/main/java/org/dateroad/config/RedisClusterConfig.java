package org.dateroad.config;

import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode.NodeFlag;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisClusterConfig {

    @Value("${aws.ip}")
    private String host;

    private RedisConnectionFactory redisConnectionFactory;
    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactoryForCluster() {
        if (this.redisConnectionFactory != null) {
            return this.redisConnectionFactory;
        }

        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration()
                .clusterNode(host, 7001)
                .clusterNode(host, 7002)
                .clusterNode(host, 7003)
                .clusterNode(host, 7004)
                .clusterNode(host, 7005)
                .clusterNode(host, 7006);
        SocketOptions socketOptions = SocketOptions.builder()
                .connectTimeout(Duration.ofSeconds(3L))
                .tcpNoDelay(true)
                .keepAlive(true)
                .build();

        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions
                .builder()
                .dynamicRefreshSources(true)
                .enableAllAdaptiveRefreshTriggers()
                .enablePeriodicRefresh(Duration.ofSeconds(30))
                .build();

        ClusterClientOptions clusterClientOptions = ClusterClientOptions
                .builder()
                .pingBeforeActivateConnection(true)
                .autoReconnect(true)
                .topologyRefreshOptions(clusterTopologyRefreshOptions)
                .nodeFilter(it ->
                        !(it.is(NodeFlag.EVENTUAL_FAIL)
                                || it.is(NodeFlag.FAIL)
                                || it.is(NodeFlag.NOADDR)
                                || it.is(NodeFlag.HANDSHAKE)))
                .validateClusterNodeMembership(false)
                .maxRedirects(5).build();

        final LettuceClientConfiguration clientConfig = LettuceClientConfiguration
                .builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .commandTimeout(Duration.ofSeconds(10L))
                .clientOptions(clusterClientOptions)
                .build();

        clusterConfig.setMaxRedirects(3);
        LettuceConnectionFactory factory = new LettuceConnectionFactory(clusterConfig, clientConfig);
        factory.setValidateConnection(false);
        factory.setShareNativeConnection(true);

        this.redisConnectionFactory = factory;  // 재사용을 위해 저장
        return factory;
    }

    @Bean
    public RedisTemplate<String, String> redistemplateForCluster() {
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

