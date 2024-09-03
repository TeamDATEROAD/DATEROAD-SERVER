package org.dateroad.config;

import io.lettuce.core.SocketOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode.NodeFlag;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${aws.ip}")
    private String host;

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
        SocketOptions socketOptions = SocketOptions.builder()
                .connectTimeout(Duration.ofMillis(200L))
                .keepAlive(true)
                .build();
        //----------------- (2) Cluster topology refresh 옵션
        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions
                .builder()
                .dynamicRefreshSources(true)
                .enableAllAdaptiveRefreshTriggers()
                .enablePeriodicRefresh(Duration.ofSeconds(30))
                .build();
        //----------------- (3) Cluster client 옵션
        ClusterClientOptions clusterClientOptions = ClusterClientOptions
                .builder()
                .pingBeforeActivateConnection(true)
                .autoReconnect(true)
                .socketOptions(socketOptions)
                .topologyRefreshOptions(clusterTopologyRefreshOptions)
                .nodeFilter(it ->
                        ! (it.is(NodeFlag.EVENTUAL_FAIL)
                        || it.is(NodeFlag.FAIL)
                        || it.is(NodeFlag.NOADDR)
                        || it.is(NodeFlag.HANDSHAKE)))
                .validateClusterNodeMembership(false)
                .maxRedirects(3).build();
        //----------------- (4) Lettuce Client 옵션
        final LettuceClientConfiguration clientConfig = LettuceClientConfiguration
                .builder()
                .commandTimeout(Duration.ofMillis(500L))
                .clientOptions(clusterClientOptions)
                .build();
        clusterConfig.setMaxRedirects(3);
        LettuceConnectionFactory factory = new LettuceConnectionFactory(clusterConfig, clientConfig);
        //----------------- (5) LettuceConnectionFactory 옵션
        factory.setValidateConnection(false);
        factory.setShareNativeConnection(false); // 클러스터
        return factory;
    }

    @Bean
    @Primary
    public RedisTemplate<String, Object> redistemplate() {
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
