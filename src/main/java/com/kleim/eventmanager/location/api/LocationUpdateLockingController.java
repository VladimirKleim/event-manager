package com.kleim.eventmanager.location.api;

import com.kleim.eventmanager.location.domain.*;
import com.kleim.eventmanager.mapper.LocationMapper;
import com.kleim.eventmanager.redis.CacheMode;
import com.kleim.eventmanager.redis.locking.RedisLockManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

@RestController
@RequestMapping("/api/location/update/lock")
public class LocationUpdateLockingController {

    private final RedisLockManager redisLockManager;
    private final LocationServiceImpl locationService;
    private final LocationMapper locationConverter;
    private final Logger log = LoggerFactory.getLogger(LocationUpdateLockingController.class);

    public LocationUpdateLockingController(RedisLockManager redisLockManager, LocationServiceImpl locationService, LocationMapper locationConverter) {
        this.redisLockManager = redisLockManager;
        this.locationService = locationService;
        this.locationConverter = locationConverter;
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDto> updateLocation(
            @PathVariable("id") Long id,
            @RequestBody LocationDto locationDto,
            @RequestParam(value = "cacheMode", defaultValue = "NONE_CACHE") CacheMode cacheMode,
            @RequestParam(defaultValue = "500") Long ms
    ) {
        log.info("Got updated location with locking by id: %s".formatted(id));
        String lockKey = "location:" + id;

        var lockIds = redisLockManager.tryLock(lockKey, Duration.ofMinutes(1));
        if (lockIds == null) {
            throw new ResponseStatusException(
                    HttpStatus.LOCKED,
                    "Блокировка уже захвачена для объекта: lockKey=%s".formatted(lockKey)
            );
        }
        try {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            var updated = locationService.updateLocation(
                    id,
                    locationConverter.toDomain(locationDto));
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(locationConverter.toDtoLocation(updated));

        } finally {
            redisLockManager.unlock(lockKey, lockIds);
        }
    }
}
