package com.kleim.eventmanager.location.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kleim.eventmanager.location.domain.*;
import com.kleim.eventmanager.mapper.LocationMapper;
import com.kleim.eventmanager.redis.CacheMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class LocationController implements LocationApi {

    private final Logger log = LoggerFactory.getLogger(LocationController.class);
    private final LocationMapper locationConverter;
    private final LocationServiceImpl locationServiceImpl;
    private final ManualLocationServiceImpl manualLocationServiceImpl;
    private final RedisSpringLocationServiceImpl redisSpringLocationServiceImpl;

    public LocationController(LocationServiceImpl locationServiceImpl, LocationMapper locationConverter, ManualLocationServiceImpl manualLocationServiceImpl, RedisSpringLocationServiceImpl redisSpringLocationServiceImpl) {
        this.locationServiceImpl = locationServiceImpl;
        this.locationConverter = locationConverter;
        this.manualLocationServiceImpl = manualLocationServiceImpl;
        this.redisSpringLocationServiceImpl = redisSpringLocationServiceImpl;
    }

    @Override
    public ResponseEntity<LocationDto> createLocation(
            LocationDto location
    ) {
        log.info("Got created location");
        var createdLocate = locationServiceImpl.createLocate(locationConverter.toDomain(location));
        return ResponseEntity.status(HttpStatus.CREATED).body(locationConverter.toDtoLocation(createdLocate));

    }

    @Override
    public ResponseEntity<LocationDto> getLocationById(
            Long id,
            CacheMode cacheMode
    ) throws JsonProcessingException {
        log.info("Got location by id: %s".formatted(id));
        LocationService locationService = resoleLocationService(cacheMode);
        var location = locationService.getLocationById(id);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(locationConverter.toDtoLocation(location));
    }

    @Override
    public List<LocationDto> getAllLocation() {
        log.info("Got all location");
        return locationServiceImpl.getAllLocations()
                .stream()
                .map(locationConverter::toDtoLocation)
                .toList();
    }

    @Override
    public ResponseEntity<Void> deleteLocation(
            Long id,
            CacheMode cacheMode
    ) {
        log.info("Got deleted location by id: %s".formatted(id));
        var locationService = resoleLocationService(cacheMode);
        locationService.deleteLocation(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<LocationDto> updateLocation(
            Long id,
            LocationDto locationDto,
            CacheMode cacheMode
    ) {
       log.info("Got updated location by id: %s".formatted(id));
       LocationService locationService = resoleLocationService(cacheMode);
       var updated = locationService.updateLocation(id, locationConverter.toDomain(locationDto));

       return ResponseEntity.status(HttpStatus.ACCEPTED).body(locationConverter.toDtoLocation(updated));
    }

    private LocationService resoleLocationService(CacheMode cacheMode) {
        return switch (cacheMode) {
            case NONE_CACHE -> locationServiceImpl;
            case MANUAL_CACHE -> manualLocationServiceImpl;
            case REDIS_CACHE -> redisSpringLocationServiceImpl;
        };
    }
}
