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
         var locEntity = locationRepository.findById(location.id()).orElseThrow(() ->
                 new IllegalArgumentException("Location was exists"));

        var createEntityLocation = locationEntityConverter.toEntity(location);
        var createdLocation = locationRepository.save(createEntityLocation);

        return locationEntityConverter.toLocation(createdLocation);
    }

    public Location getLocationById(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new NoSuchElementException("Location with id " + id + " not found");
        }
        var gotId = locationRepository.getById(id);
        return locationEntityConverter.toLocation(gotId);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll().stream().map(locationEntityConverter::toLocation).toList();
    }

    public Location deleteLocation(Long id) {
        var deleteLocation = locationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Location with id " + id + " not found"));
        locationRepository.deleteById(id);
      return locationEntityConverter.toLocation(deleteLocation);
    }

    public Location updateLocation(Long id, Location location) {
        if (location.id() != null) {
            throw new NoSuchElementException("Location with id :" + id + " not found");
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
