package com.kleim.eventmanager.service;

import com.kleim.eventmanager.auth.UserRole;
import com.kleim.eventmanager.entity.EventEntity;
import com.kleim.eventmanager.model.event.*;
import com.kleim.eventmanager.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


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

    @Transactional
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
        if (updateRequestDto.locationId() != null) {
            var locationId = Optional.ofNullable(updateRequestDto.locationId()).orElse(event.locationId());
            var location = locationService.getLocationById(locationId);
            var maxPlaces = Optional.ofNullable(updateRequestDto.maxPlace()).orElse(event.maxPlace());
            if (maxPlaces > location.capacity()) {
                throw new IllegalArgumentException("Capacity must not be more than max places");
            }
        }
        if (event.maxPlace() != null && event.registrationList().size() > updateRequestDto.maxPlace()) {
            throw new IllegalArgumentException("All places reserved");
        }


//        var upd = eventRepository.updateEvent(
//                Optional.ofNullable(updateRequestDto.name()).orElse(event.name()),
//                Optional.ofNullable(updateRequestDto.maxPlace()).orElse(event.maxPlace(),
//                Optional.ofNullable(updateRequestDto.cost()).orElse(event.cost()),
//                Optional.ofNullable(updateRequestDto.duration()).orElse(event.duration()),
//                Optional.ofNullable(updateRequestDto.locationId()).orElse(event.locationId())
//        );
        return null;

    }




    private void checkCurrentAccessEvent(Long eventId) {
        var currentUser = authenticationService.getCurrentAuthUser();
        var foundId = findEventById(eventId);
        if (!foundId.ownerId().equals(currentUser.id()) || !currentUser.role().equals(UserRole.ADMIN)) {
            throw new IllegalArgumentException("Forbidden, access denied");
        }
    }
}
