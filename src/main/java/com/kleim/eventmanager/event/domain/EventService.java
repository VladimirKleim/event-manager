package com.kleim.eventmanager.event.domain;

import com.kleim.eventmanager.auth.domain.UserRole;
import com.kleim.eventmanager.event.db.EventEntity;
import com.kleim.eventmanager.event.db.EventRepository;
import com.kleim.eventmanager.location.db.LocationRepository;
import com.kleim.eventmanager.location.domain.LocationService;
import com.kleim.eventmanager.auth.domain.AuthenticationService;
import com.kleim.eventmanager.auth.domain.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class EventService {

    private final EventRepository eventRepository;
    private final LocationService locationService;
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final EventEntityConverter eventEntityConverter;
    private final LocationRepository locationRepository;


    public EventService(EventRepository eventRepository, LocationService locationService, UserService userService, AuthenticationService authenticationService, EventEntityConverter eventEntityConverter, LocationRepository locationRepository) {
        this.eventRepository = eventRepository;
        this.locationService = locationService;
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.eventEntityConverter = eventEntityConverter;
        this.locationRepository = locationRepository;
    }


    public Event eventCreate(EventCreateRequest eventCreateRequest) {

       var user = authenticationService.getCurrentAuthUser();
       var location = locationService.getLocationById(eventCreateRequest.locationId());

       if (location.capacity() < eventCreateRequest.maxPlace()) {
           throw new IllegalArgumentException("Location is crowded. Capacity=%s, max places=%s"
                   .formatted(location.capacity(), eventCreateRequest.maxPlace()));
       }

       var eventEntity = new EventEntity(
               null,
               eventCreateRequest.name(),
               user.id(),
               eventCreateRequest.maxPlace(),
               List.of(),
               eventCreateRequest.date(),
               eventCreateRequest.cost(),
               eventCreateRequest.duration(),
               eventCreateRequest.locationId(),
               EventStatus.WAIT_START
       );
       var savedEntity = eventRepository.save(eventEntity);

       return eventEntityConverter.toDomain(savedEntity);
    }


    public Event getEventById(Long eventId) {
        var gotEvent = eventRepository.findById(eventId).orElseThrow(() ->
                new IllegalArgumentException("Event does not exist"));
        return eventEntityConverter.toDomain(gotEvent);
    }


    @Transactional
    public void cancelEventById(Long eventId) {
     checkAccessToModifyEvent(eventId);
     var event = getEventById(eventId);
     if (event.id() != null) {
         if (event.status().equals(EventStatus.CANCELLED)) {
            throw new IllegalArgumentException("Event is already over.");
         }
         if (event.status().equals(EventStatus.FINISHED) || event.status().equals(EventStatus.STARTED)) {
             throw new IllegalArgumentException("Event has been started or finished");
         }
         //soft delete
         eventRepository.changeEventStatus(eventId, EventStatus.CANCELLED);
     }
    }


    public void checkAccessToModifyEvent(Long eventId) {
        var user = authenticationService.getCurrentAuthUser();
        var event = getEventById(eventId);
        if (event.ownerId().equals(user.id()) || user.role().equals(UserRole.ADMIN)) {
            return;
        } else {
            throw new IllegalArgumentException("Forbidden, access denied");
        }
    }


    public List<Event> getAllEvents() {
        var gotAllEvents = eventRepository.findAll();
        return gotAllEvents.stream()
                .map(eventEntityConverter::toDomain)
                .toList();
    }


    @Transactional
    public Event updateEvent(Long eventId, EventUpdateRequest updateRequest) {
        checkAccessToModifyEvent(eventId);
        var event = getEventById(eventId);
        var location = locationService.getLocationById(
                Optional.ofNullable(updateRequest.locationId()).orElse(event.locationId())
        );

        if (event.status().equals(EventStatus.CANCELLED)) {
            throw new IllegalArgumentException("Event is already over.");
        }
        if (event.status().equals(EventStatus.FINISHED) || event.status().equals(EventStatus.STARTED)) {
            throw new IllegalArgumentException("Event has been started or finished");
        }
        if (updateRequest.maxPlace() != null && updateRequest.locationId() != null) {

            var locationId = Optional.ofNullable(updateRequest.locationId())
                    .orElse(event.locationId());
            var maxPlaces = Optional.ofNullable(updateRequest.maxPlace()).orElse(event.maxPlace());
            var locate = locationService.getLocationById(locationId);
            if (locate.capacity() < maxPlaces) {
                throw new IllegalArgumentException("Location is crowded. Capacity=%s, max places=%s"
                        .formatted(location.capacity(), updateRequest.maxPlace()));
            }

        }

        if (updateRequest.maxPlace() != null  &&
                event.registrationList().size() > updateRequest.maxPlace()) {
            throw new IllegalArgumentException("There are no places yet");
        }

        eventRepository.updateEvent(
                eventId,
                Optional.ofNullable(updateRequest.name()).orElse(event.name()),
                Optional.ofNullable(updateRequest.maxPlace()).orElse(event.maxPlace()),
                Optional.ofNullable(updateRequest.date()).orElse(event.date()),
                Optional.ofNullable(updateRequest.cost()).orElse(event.cost()),
                Optional.ofNullable(updateRequest.duration()).orElse(event.duration()),
                Optional.ofNullable(updateRequest.locationId()).orElse(event.locationId())
        );

        return getEventById(eventId);
    }


    @Transactional(readOnly = true) //DML
    public List<Event> searchFilter(EventSearchRequest searchRequest) {

        var foundedEvents = eventRepository.searchAllEventsByFilter(
                searchRequest.name(),
                searchRequest.placesMin(),
                searchRequest.placesMax(),
                searchRequest.dateStartAfter(),
                searchRequest.dateStartBefore(),
                searchRequest.costMin(),
                searchRequest.costMax(),
                searchRequest.durationMin(),
                searchRequest.durationMax(),
                searchRequest.locationId(),
                searchRequest.eventStatus()
        );

        return foundedEvents.stream()
                .map(eventEntityConverter::toDomain)
                .toList();
    }


    @Transactional(readOnly = true)
    public List<Event> getOwner() {
        var user = authenticationService.getCurrentAuthUser();
     var event = eventRepository.findAllByOwnerId(user.id());
     return event.stream()
             .map(eventEntityConverter::toDomain)
             .toList();
    }
}
