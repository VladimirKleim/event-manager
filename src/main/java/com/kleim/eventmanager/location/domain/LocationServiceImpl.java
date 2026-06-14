package com.kleim.eventmanager.location.domain;

import com.kleim.eventmanager.location.db.LocationEntity;
import com.kleim.eventmanager.location.db.LocationRepository;
import com.kleim.eventmanager.mapper.LocationDbMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationDbMapper locationEntityConverter;
    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationDbMapper locationEntityConverter, LocationRepository locationRepository) {
        this.locationEntityConverter = locationEntityConverter;
        this.locationRepository = locationRepository;
    }

    @Override
    public Location createLocate(Location location) {
        if (location.id() != null) {
            throw new IllegalArgumentException("Can not create location with provided id. Id Must be empty");
        }

        var createEntityLocation = locationEntityConverter.toEntity(location);
        var createdLocation = locationRepository.save(createEntityLocation);

        return locationEntityConverter.toLocation(createdLocation);
    }

    @Override
    public Location getLocationById(Long id) {
        var locationEntity = locationRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Location with id=%s not exist".formatted(id)));
        return locationEntityConverter.toLocation(locationEntity);
    }

    @Override
    public List<Location> getAllLocations() {
        return locationRepository.findAll().stream().map(locationEntityConverter::toLocation).toList();
    }

    @Override
    public void deleteLocation(Long id) {
        locationRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("Location with id " + id + " not found"));
        locationRepository.deleteById(id);

    }

    @Override
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
