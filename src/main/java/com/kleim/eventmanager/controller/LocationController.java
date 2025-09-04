package com.kleim.eventmanager.controller;


import com.kleim.eventmanager.model.LocationConverter;
import com.kleim.eventmanager.model.LocationDto;
import com.kleim.eventmanager.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
