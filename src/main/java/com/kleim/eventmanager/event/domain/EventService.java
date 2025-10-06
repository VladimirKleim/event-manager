package com.kleim.eventmanager.event.domain;

import com.kleim.eventmanager.auth.domain.UserRole;
import com.kleim.eventmanager.event.db.EventRepository;
import com.kleim.eventmanager.location.domain.LocationService;
import com.kleim.eventmanager.auth.domain.AuthenticationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class EventService {

    private final EventRepository eventRepository;
    private final LocationService locationService;
    private final AuthenticationService authenticationService;
    private final EventEntityConverter eventEntityConverter;
    private final EventCreateMapper eventCreateMapper;
    private final EventUpdateMapper eventUpdateMapper;


    public EventService(EventRepository eventRepository, LocationService locationService, AuthenticationService authenticationService, EventEntityConverter eventEntityConverter, EventCreateMapper eventCreateMapper, EventUpdateMapper eventUpdateMapper) {
        this.eventRepository = eventRepository;
        this.locationService = locationService;
        this.authenticationService = authenticationService;
        this.eventEntityConverter = eventEntityConverter;
        this.eventCreateMapper = eventCreateMapper;
        this.eventUpdateMapper = eventUpdateMapper;
    }


    public Event eventCreate(EventCreateRequest eventCreateRequest) {

       var user = authenticationService.getCurrentAuthUser();
       var location = locationService.getLocationById(eventCreateRequest.locationId());

       if (location.capacity() < eventCreateRequest.maxPlace()) {
           throw new IllegalArgumentException("Location is crowded. Capacity=%s, max places=%s"
                   .formatted(location.capacity(), eventCreateRequest.maxPlace()));
       }

       var eventEntity = eventCreateMapper.toEntity(eventCreateRequest, user.id());
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

        var updatedEvent = eventUpdateMapper.updateEventFields(event, updateRequest);
        var updatedEntity = eventEntityConverter.toEntity(updatedEvent);
        eventRepository.save(updatedEntity);

        return updatedEvent;
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
