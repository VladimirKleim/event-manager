package com.kleim.eventmanager.service;


import com.kleim.eventmanager.model.Location;
import com.kleim.eventmanager.model.LocationEntityConverter;
import com.kleim.eventmanager.repository.LocationRepository;
import org.springframework.stereotype.Service;

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
}
