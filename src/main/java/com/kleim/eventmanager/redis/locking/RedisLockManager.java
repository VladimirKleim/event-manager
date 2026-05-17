package com.kleim.eventmanager.redis.locking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;

@Service
public class RedisLockManager {
    private final StringRedisTemplate stringRedisTemplate;
    private final Logger log = LoggerFactory.getLogger(RedisLockManager.class);

    private static final String RELEASE_LOCK_LUA_SCRIPT = """
            if redis.call('GET', KEYS[1]) == ARGV[1] then
              return redis.call('DEL', KEYS[1])
            else return 0 end
            """;

    public RedisLockManager(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public String tryLock(
         String key,
         Duration ttl
    ) {
       String lockKey = "lock:" + key;
       String lockIds = UUID.randomUUID().toString();
       Boolean isLockSuccessful = stringRedisTemplate.opsForValue().setIfAbsent(
               lockKey, lockIds, ttl
       );
       if (Boolean.TRUE.equals(isLockSuccessful)) {
           log.info("Lock has been acquired for: {}, {}", lockKey, lockIds);
           return lockIds;
       }
       return null;
    }

    public void unlock(
            String key,
            String lockIds
    ) {
        log.info("Trying to unlock lock with lockIds:{}", lockIds);
        String lockKey = "lock:" + key;
        Long result = stringRedisTemplate.execute(connection -> connection.scriptingCommands().eval(
                RELEASE_LOCK_LUA_SCRIPT.getBytes(StandardCharsets.UTF_8),
                ReturnType.INTEGER,
                1,
                lockKey.getBytes(StandardCharsets.UTF_8),
                lockIds.getBytes(StandardCharsets.UTF_8)
        ), true);

        if (result != null && result == 1L) {
            log.info("Lock has been release for: {}, {}", lockKey, lockIds);
        } else {
            log.info("Lock was already released or re-acquired: {}, {}", lockKey, lockIds);
        }
    }
}
