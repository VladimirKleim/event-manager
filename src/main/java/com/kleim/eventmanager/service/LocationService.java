package com.kleim.eventmanager.service;


import com.kleim.eventmanager.entity.LocationEntity;
import com.kleim.eventmanager.model.Location;
import com.kleim.eventmanager.model.LocationEntityConverter;
import com.kleim.eventmanager.repository.LocationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationEntityConverter locationEntityConverter;
    private final LocationRepository locationRepository;

    public LocationService(LocationEntityConverter locationEntityConverter, LocationRepository locationRepository) {
        this.locationEntityConverter = locationEntityConverter;
        this.locationRepository = locationRepository;
    }


    public Location createLocate(Location location) {
//        if (!locationRepository.existsById(location.id())) {
//            throw new IllegalArgumentException("todo");
//        }

        var createEntityLocation = locationEntityConverter.toEntity(location);
        var createdLocation = locationRepository.save(createEntityLocation);

        return locationEntityConverter.toLocation(createdLocation);
    }

    public Location getLocationById(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new NoSuchElementException("TODO");
        }
        var gotId = locationRepository.getById(id);
        return locationEntityConverter.toLocation(gotId);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll().stream().map(locationEntityConverter::toLocation).toList();
    }

    public void deleteLocation(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new NoSuchElementException("TODO");
        }
        locationRepository.deleteById(id);
    }

    public Location updateLocation(Long id, Location location) {
        if (!locationRepository.existsById(id)) {
            throw new NoSuchElementException("TODO");
        }
        var updatedEntity = new LocationEntity(
                id,
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
        return locationEntityConverter.toLocation(locationRepository.save(updatedEntity));
    }
}
