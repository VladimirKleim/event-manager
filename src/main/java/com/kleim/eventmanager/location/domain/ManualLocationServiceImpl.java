package com.kleim.eventmanager.location.domain;

import com.kleim.eventmanager.location.db.LocationEntity;
import com.kleim.eventmanager.location.db.LocationRepository;
import com.kleim.eventmanager.mapper.LocationDbMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class ManualLocationServiceImpl implements LocationService {

    private final LocationDbMapper locationDbMapper;
    private final LocationRepository locationRepository;
    private final Logger logger = LoggerFactory.getLogger(ManualLocationServiceImpl.class);
    private final RedisTemplate<String, LocationEntity> redisTemplateLocation;

    private final String CACHE_PREFIX = "location:";

    public ManualLocationServiceImpl(LocationDbMapper locationDbMapper, LocationRepository locationRepository, RedisTemplate<String, LocationEntity> redisTemplateLocation) {
        this.locationDbMapper = locationDbMapper;
        this.locationRepository = locationRepository;
        this.redisTemplateLocation = redisTemplateLocation;
    }

    @Override
    public Location createLocate(Location location) {
        if (location.id() != null) {
            throw new IllegalArgumentException("Can not create location with provided id. Id Must be empty");
        }

        var createEntityLocation = locationDbMapper.toEntity(location);
        var createdLocation = locationRepository.save(createEntityLocation);

        redisTemplateLocation.opsForValue().set(CACHE_PREFIX + createdLocation.getId(), createdLocation, Duration.ofMinutes(5));

        return locationDbMapper.toLocation(createdLocation);
    }

    @Override
    public Location getLocationById(Long id) {
        logger.info("Getting location: id={}", id);
        var cacheKey = CACHE_PREFIX + id;

        LocationEntity entityFromCache = redisTemplateLocation.opsForValue()
                .get(cacheKey);

        if (entityFromCache != null) {
            logger.info("Location found in Cache: id={}", id);
            return locationDbMapper.toLocation(
                    entityFromCache
            );
        }
        logger.info("Location NOT found in Cache: id={}", id);

        var locationEntity = locationRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Location with id=%s not exist".formatted(id)));

        redisTemplateLocation.opsForValue().set(cacheKey, locationEntity, Duration.ofMinutes(10));
        logger.info("Location add in Cache: id={}", id);

        return locationDbMapper.toLocation(locationEntity);
    }

    @Override
    public List<Location> getAllLocations() {
        return locationRepository.findAll()
                .stream()
                .map(locationDbMapper::toLocation)
                .toList();
    }

    @Override
    public void deleteLocation(Long id) {
        logger.info("Delete location from DB: id={}", id);
        if (!locationRepository.existsById(id)) {
            throw new IllegalArgumentException("Location with id " + id + " not found");
        }
        locationRepository.deleteById(id);

        redisTemplateLocation.delete(CACHE_PREFIX + id);
        logger.info("Cache invalidated for delete location from cache id={}", id);
    }

    @Override
    public Location updateLocation(Long id, Location location) {
        if (location.id() != null ) {
            throw new IllegalArgumentException("Id must be null.");
        }
        String cacheKey = CACHE_PREFIX + id;
        var existLocation = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location with id=%s not found".formatted(id)));

        var updatedEntity = new LocationEntity(
                existLocation.getId(),
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
        logger.info("Cache invalidated for update location id={}", id);

        var updatedLocation = locationRepository.save(updatedEntity);
        redisTemplateLocation.opsForValue().set(cacheKey, updatedLocation, Duration.ofMinutes(10));

        return locationDbMapper.toLocation(updatedLocation);
    }
}
