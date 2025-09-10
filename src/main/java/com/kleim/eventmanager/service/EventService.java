package com.kleim.eventmanager.service;

import com.kleim.eventmanager.auth.UserRole;
import com.kleim.eventmanager.entity.EventEntity;
import com.kleim.eventmanager.model.event.*;
import com.kleim.eventmanager.repository.EventRepository;
import com.kleim.eventmanager.repository.UserRepository;
import com.kleim.eventmanager.security.token.JwtTokenFilter;
import com.kleim.eventmanager.security.token.JwtTokenManager;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public Event findEventById(Long eventId) {
        var foundId = eventRepository.findById(eventId);
        return eventEntityConverter.toDomain(foundId.orElseThrow(() ->
                new IllegalArgumentException("Not found id: %s".formatted(eventId))));
    }

    public void deleteEvent(Long eventId) {

       checkCurrentAccessEvent(eventId);
        var event = findEventById(eventId);
        if (event.status().equals(EventStatus.CANCELLED)) {
            return;
        }
        if (event.status().equals(EventStatus.FINISHED) || event.status().equals(EventStatus.STARTED)) {
            throw new IllegalArgumentException("Can't cancel event when started");
        }

        eventRepository.changeEventStatus(eventId, EventStatus.CANCELLED);

    }

    public Event eventUpdate(Long eventId, EventUpdateRequestDto updateRequestDto) {
        checkCurrentAccessEvent(eventId);

        var event = findEventById(eventId);
        if (!event.status().equals(EventStatus.WAIT_START)) {
            throw new IllegalArgumentException("Cannot update event before start");
        }
    }




    private void checkCurrentAccessEvent(Long eventId) {
        var currentUser = authenticationService.getCurrentAuthUser();
        var foundId = findEventById(eventId);
        if (!foundId.ownerId().equals(currentUser.id()) || !currentUser.role().equals(UserRole.ADMIN)) {
            throw new IllegalArgumentException("Forbidden, access denied");
        }
    }
}
