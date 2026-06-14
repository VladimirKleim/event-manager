package com.kleim.eventmanager.location.domain;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

/**
 * Сервис для работы с локациями.
 */
public interface LocationService {

    Location createLocate(Location location);

    Location getLocationById(Long id) throws JsonProcessingException;

    List<Location> getAllLocations();

    void deleteLocation(Long id);

    Location updateLocation(Long id, Location location);

}
