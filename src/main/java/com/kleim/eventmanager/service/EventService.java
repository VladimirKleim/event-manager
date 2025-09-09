package com.kleim.eventmanager.service;

import com.kleim.eventmanager.entity.EventEntity;
import com.kleim.eventmanager.model.event.*;
import com.kleim.eventmanager.repository.EventRepository;
import com.kleim.eventmanager.security.token.JwtTokenFilter;
import com.kleim.eventmanager.security.token.JwtTokenManager;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final  LocationService locationService;
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final EventEntityConverter eventEntityConverter;

    public EventService(EventRepository eventRepository, LocationService locationService, UserService userService, AuthenticationService authenticationService, EventEntityConverter eventEntityConverter) {
        this.eventRepository = eventRepository;
        this.locationService = locationService;
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.eventEntityConverter = eventEntityConverter;
    }


    public Event eventCreate(EventRequestDto event) {
        var location = locationService.getLocationById(event.locationId());
        if (location.capacity() < event.maxPlace()) {
            throw new IllegalArgumentException("Capacity should not less than max place. Capacity of location=%s"
                    .formatted(location.capacity()));
        }
        var currentUser = authenticationService.getCurrentAuthUser();
        var savedEvent = new EventEntity(
                null,
                event.name(),
                currentUser.id(),
                event.maxPlace(),
                List.of(),
                event.date(),
                event.cost(),
                event.duration(),
                event.locationId(),
                EventStatus.WAIT_START
        );
       var createEvent = eventRepository.save(savedEvent);
       return eventEntityConverter.toDomain(createEvent);
    }
}
