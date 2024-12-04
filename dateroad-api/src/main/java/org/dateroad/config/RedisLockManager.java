package org.dateroad.config;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisLockManager {
    private final StringRedisTemplate redisTemplate;

    public RedisLockManager(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Boolean acquireLock(String key, String lockType, long timeout) {
        return redisTemplate.opsForValue().setIfAbsent(key, lockType, Duration.ofSeconds(timeout));
    }

    public void releaseLock(String key) {
        redisTemplate.delete(key);
    }
}
