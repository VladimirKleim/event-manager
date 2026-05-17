package com.kleim.eventmanager.location.domain;

import com.kleim.eventmanager.location.db.LocationEntity;
import com.kleim.eventmanager.location.db.LocationRepository;
import com.kleim.eventmanager.mapper.LocationDbMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisSpringLocationServiceImpl implements LocationService {

    private final LocationDbMapper locationDbMapper;
    private final LocationRepository locationRepository;
    private final Logger logger = LoggerFactory.getLogger(ManualLocationServiceImpl.class);
    private final RedisTemplate<String, LocationEntity> redisTemplateLocation;

    public RedisSpringLocationServiceImpl(LocationDbMapper locationDbMapper, LocationRepository locationRepository, RedisTemplate<String, LocationEntity> redisTemplateLocation) {
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

        return locationDbMapper.toLocation(createdLocation);
    }

    @Override
    @Cacheable(
            value = "location",
            key = "#id"
    )
    public Location getLocationById(Long id) {
        logger.info("Getting location: id={}", id);
        var locationEntity = locationRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Location with id=%s not exist".formatted(id)));
        return locationDbMapper.toLocation(locationEntity);
    }

    @Override
    public List<Location> getAllLocations() {
        return locationRepository.findAll().stream().map(locationDbMapper::toLocation).toList();
    }

    @Override
    @CacheEvict(
            value = "location",
            key = "#id"
    )
    public void deleteLocation(Long id) {
        logger.info("Delete location from DB: id={}", id);
        if (!locationRepository.existsById(id)) {
            throw new IllegalArgumentException("Location with id " + id + " not found");
        }
        locationRepository.deleteById(id);
    }

    @Override
    @CachePut(
            value = "location",
            key = "#id"
    )
    public Location updateLocation(Long id, Location location) {
        if (location.id() != null ) {
            throw new IllegalArgumentException("Id must be null.");
        }
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

        return locationDbMapper.toLocation(updatedLocation);
    }
}
