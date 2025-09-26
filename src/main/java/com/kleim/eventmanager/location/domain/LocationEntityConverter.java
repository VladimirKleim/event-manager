package com.kleim.eventmanager.location.domain;

import com.kleim.eventmanager.location.db.LocationEntity;
import org.springframework.stereotype.Component;

@Component
public final class LocationEntityConverter {

    public LocationEntity toEntity(Location location) {
        return new LocationEntity(
                location.id(),
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
    }

    public Location toLocation(LocationEntity location) {
        return new Location(
                location.getId(),
                location.getName(),
                location.getAddress(),
                location.getCapacity(),
                location.getDescription()
        );
    }
}
