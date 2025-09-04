package com.kleim.eventmanager.controller;


import com.kleim.eventmanager.model.LocationConverter;
import com.kleim.eventmanager.model.LocationDto;
import com.kleim.eventmanager.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private final Logger log = LoggerFactory.getLogger(LocationController.class);
    private final LocationService locationService;
    private final LocationConverter locationConverter;

    public LocationController(LocationService locationService, LocationConverter locationConverter) {
        this.locationService = locationService;
        this.locationConverter = locationConverter;
    }

    @PostMapping
    public ResponseEntity<LocationDto> createLocation(
            @RequestBody LocationDto location
    ) {
        log.info("todo");
        var createdLocate = locationService.createLocate(locationConverter.toDomain(location));
        return ResponseEntity.status(HttpStatus.CREATED).body(locationConverter.toDtoLocation(createdLocate));
    }


    @GetMapping("/{id}")
    public ResponseEntity<LocationDto> getLocationById(
            @PathVariable("id") Long id
    ) {
        log.info("todo");
        var loc = locationService.getLocationById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(locationConverter.toDtoLocation(loc));
    }


    @GetMapping
    public List<LocationDto> getAllLocation(

    ) {
        log.info("TODO");
        return locationService.getAllLocations().stream().map(locationConverter::toDtoLocation).toList();
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteLocation(
            @PathVariable("id") Long id
    ) {
        locationService.deleteLocation(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PutMapping("{id}")
    public ResponseEntity<LocationDto> updateLocation(
            @PathVariable("id") Long id,
            @RequestBody LocationDto locationDto
    ) {
       log.info("TODO");
       var updated = locationService.updateLocation(id, locationConverter.toDomain(locationDto));
       return ResponseEntity.status(HttpStatus.ACCEPTED).body(locationConverter.toDtoLocation(updated));
    }


}
