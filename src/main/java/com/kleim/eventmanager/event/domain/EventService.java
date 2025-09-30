package com.kleim.eventmanager.event.domain;

import com.kleim.eventmanager.auth.domain.UserRole;
import com.kleim.eventmanager.event.NotificationService;
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
    private final NotificationService notificationService;

    private final EventCreateMapper eventCreateMapper;


    public EventService(EventRepository eventRepository, LocationService locationService, UserService userService, AuthenticationService authenticationService, EventEntityConverter eventEntityConverter, LocationRepository locationRepository, NotificationService notificationService, EventCreateMapper eventCreateMapper) {
        this.eventRepository = eventRepository;
        this.locationService = locationService;
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.eventEntityConverter = eventEntityConverter;
        this.locationRepository = locationRepository;
        this.notificationService = notificationService;
        this.eventCreateMapper = eventCreateMapper;
    }


    public Event eventCreate(EventCreateRequest eventCreateRequest) {

       var user = authenticationService.getCurrentAuthUser();
       var location = locationService.getLocationById(eventCreateRequest.locationId());

       if (location.capacity() < eventCreateRequest.maxPlace()) {
           throw new IllegalArgumentException("Location is crowded. Capacity=%s, max places=%s"
                   .formatted(location.capacity(), eventCreateRequest.maxPlace()));
       }

       var eventEntity = eventCreateMapper.toEntity(user.id(), eventCreateRequest);
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

         notificationService.changeEventStatus(event.id(), EventStatus.CANCELLED);
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
        var event = eventRepository.findById(eventId).orElseThrow();
        var location = locationService.getLocationById(
                Optional.ofNullable(updateRequest.locationId()).orElse(event.getLocationId())
        );

        if (event.getStatus().equals(EventStatus.CANCELLED)) {
            throw new IllegalArgumentException("Event is already over.");
        }
        if (event.getStatus().equals(EventStatus.FINISHED) || event.getStatus().equals(EventStatus.STARTED)) {
            throw new IllegalArgumentException("Event has been started or finished");
        }
        if (updateRequest.maxPlace() != null && updateRequest.locationId() != null) {

            var locationId = Optional.ofNullable(updateRequest.locationId())
                    .orElse(event.getLocationId());
            var maxPlaces = Optional.ofNullable(updateRequest.maxPlace()).orElse(event.getMaxPlace());
            var locate = locationService.getLocationById(locationId);
            if (locate.capacity() < maxPlaces) {
                throw new IllegalArgumentException("Location is crowded. Capacity=%s, max places=%s"
                        .formatted(location.capacity(), updateRequest.maxPlace()));
            }

        }

        if (updateRequest.maxPlace() != null  &&
                event.getRegistrationList().size() > updateRequest.maxPlace()) {
            throw new IllegalArgumentException("There are no places yet");
        }

        notificationService.ChangeAllEventsFields(event.getId(), updateRequest);

                Optional.ofNullable(updateRequest.name()).filter(e -> !e.equals(event.getName()))
                        .ifPresent(e -> event.setName(updateRequest.name()));
                Optional.ofNullable(updateRequest.maxPlace()).filter(e -> !e.equals(event.getMaxPlace()))
                        .ifPresent(e -> event.setMaxPlace(updateRequest.maxPlace()));
                Optional.ofNullable(updateRequest.date()).filter(e -> !e.equals(event.getDate()))
                        .ifPresent(e -> event.setDate(updateRequest.date()));
                Optional.ofNullable(updateRequest.cost()).filter(e -> !e.equals(event.getCost()))
                        .ifPresent(e -> event.setCost(updateRequest.cost()));
                Optional.ofNullable(updateRequest.duration()).filter(e -> !e.equals(event.getDuration()))
                        .ifPresent(e -> event.setDuration(updateRequest.duration()));
                Optional.ofNullable(updateRequest.locationId()).filter(e -> !e.equals(event.getLocationId()))
                        .ifPresent(e -> event.setLocationId(updateRequest.locationId()));

                eventRepository.save(event);

        return eventEntityConverter.toDomain(event);
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
