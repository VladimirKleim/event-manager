package com.kleim.eventmanager.redis.ratelimit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class FixedWindowRateLimiter {

    private final StringRedisTemplate redisTemplate;

    public FixedWindowRateLimiter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean allowRequest(
            String clientId,
            int limit,
            Duration windowSize
    ) {
        long windowIndex = System.currentTimeMillis() / windowSize.toMillis();
        String key = String.format("rate:%s:%s", clientId, windowIndex);

        Long countHits = redisTemplate.opsForValue().increment(key);

        if (countHits != null && countHits == 1L) {
            redisTemplate.expire(key, windowSize);
        }

        return countHits != null && countHits <= limit;
    }
}
