package com.kleim.eventmanager.event.domain;

import com.kleim.eventmanager.auth.domain.UserRole;
import com.kleim.eventmanager.event.db.EventEntity;
import com.kleim.eventmanager.event.db.EventRepository;
import com.kleim.eventmanager.location.db.LocationRepository;
import com.kleim.eventmanager.location.domain.LocationService;
import com.kleim.eventmanager.auth.domain.AuthenticationService;
import com.kleim.eventmanager.auth.domain.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Security;
import java.util.List;
import java.util.Objects;
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


    public Event eventCreate(EventRequestDto eventRequestDto) {

       var user = authenticationService.getCurrentAuthUser();
       var location = locationService.getLocationById(eventRequestDto.locationId());

       if (location.capacity() < eventRequestDto.maxPlace()) {
           throw new IllegalArgumentException("Location is crowded. Capacity=%s, max places=%s"
                   .formatted(location.capacity(),eventRequestDto.maxPlace()));
       }

       var eventEntity = new EventEntity(
               null,
               eventRequestDto.name(),
               user.id(),
               eventRequestDto.maxPlace(),
               List.of(),
               eventRequestDto.date(),
               eventRequestDto.cost(),
               eventRequestDto.duration(),
               eventRequestDto.locationId(),
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
    public Event updateEvent(Long eventId, EventUpdateRequestDto updateRequestDto) {
        checkAccessToModifyEvent(eventId);
        var event = getEventById(eventId);
        var location = locationService.getLocationById(
                Optional.ofNullable(updateRequestDto.locationId()).orElse(event.locationId())
        );

        if (event.status().equals(EventStatus.CANCELLED)) {
            throw new IllegalArgumentException("Event is already over.");
        }
        if (event.status().equals(EventStatus.FINISHED) || event.status().equals(EventStatus.STARTED)) {
            throw new IllegalArgumentException("Event has been started or finished");
        }
        if (updateRequestDto.maxPlace() != null && updateRequestDto.locationId() != null) {

            var locationId = Optional.ofNullable(updateRequestDto.locationId())
                    .orElse(event.locationId());
            var maxPlaces = Optional.ofNullable(updateRequestDto.maxPlace()).orElse(event.maxPlace());
            var locate = locationService.getLocationById(locationId);
            if (locate.capacity() < maxPlaces) {
                throw new IllegalArgumentException("Location is crowded. Capacity=%s, max places=%s"
                        .formatted(location.capacity(), updateRequestDto.maxPlace()));
            }

        }

        if (updateRequestDto.maxPlace() != null  &&
                event.registrationList().size() > updateRequestDto.maxPlace()) {
            throw new IllegalArgumentException("There are no places yet");
        }

        eventRepository.updateEvent(
                eventId,
                Optional.ofNullable(updateRequestDto.name()).orElse(event.name()),
                Optional.ofNullable(updateRequestDto.maxPlace()).orElse(event.maxPlace()),
                Optional.ofNullable(updateRequestDto.date()).orElse(event.date()),
                Optional.ofNullable(updateRequestDto.cost()).orElse(event.cost()),
                Optional.ofNullable(updateRequestDto.duration()).orElse(event.duration()),
                Optional.ofNullable(updateRequestDto.locationId()).orElse(event.locationId())
        );

        return getEventById(eventId);
    }


    @Transactional(readOnly = true) //DML
    public List<Event> searchFilter(EventSearchRequestDto searchRequestDto) {

        var foundedEvents = eventRepository.searchAllEventsByFilter(
                searchRequestDto.name(),
                searchRequestDto.placesMin(),
                searchRequestDto.placesMax(),
                searchRequestDto.dateStartAfter(),
                searchRequestDto.dateStartBefore(),
                searchRequestDto.costMin(),
                searchRequestDto.costMax(),
                searchRequestDto.durationMin(),
                searchRequestDto.durationMax(),
                searchRequestDto.locationId(),
                searchRequestDto.eventStatus()
        );

        return foundedEvents.stream()
                .map(eventEntityConverter::toDomain)
                .toList();
    }


    @Transactional(readOnly = true)
    public List<Event> getOwner() {
        var user = authenticationService.getCurrentAuthUser();
     var event = eventRepository.getOwner(user.id());
     return event.stream()
             .map(eventEntityConverter::toDomain)
             .toList();
    }
}
