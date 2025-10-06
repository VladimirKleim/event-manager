package com.kleim.eventmanager.event.domain;

import com.kleim.eventmanager.auth.domain.AuthenticationService;
import com.kleim.eventmanager.auth.pojo.User;
import com.kleim.eventmanager.event.db.EventRegisterEntity;
import com.kleim.eventmanager.event.db.EventRegisterRepository;
import com.kleim.eventmanager.event.db.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventRegistrationService {

    private final AuthenticationService authenticationService;
    private final EventService eventService;
    private final EventRegisterRepository eventRegisterRepository;
    private final EventRepository eventRepository;
    private final EventEntityConverter eventEntityConverter;


    public EventRegistrationService(AuthenticationService authenticationService, EventService eventService, EventRegisterRepository eventRegisterRepository, EventRepository eventRepository, EventEntityConverter eventEntityConverter) {
        this.authenticationService = authenticationService;
        this.eventService = eventService;
        this.eventRegisterRepository = eventRegisterRepository;
        this.eventRepository = eventRepository;
        this.eventEntityConverter = eventEntityConverter;
    }


    public void registerToEvent(Long eventId, User user) {
        var event = eventService.getEventById(eventId);
        var eventById = eventRepository.findById(event.id()).orElseThrow(() ->
                new IllegalArgumentException("No such event with id=%s".formatted(event.id())));
        if (user.id().equals(event.ownerId())) {
            throw new IllegalArgumentException("Event creator cannot become member");
        }
        if (!event.status().equals(EventStatus.WAIT_START)) {
            throw new IllegalArgumentException("The event has started");
        }

        var findRegistration = eventRegisterRepository.findByEventIdAndUserId(user.id(), eventId);
        if (findRegistration.isPresent()) {
            throw new IllegalArgumentException("User already registered to event");
        }

        var gotRegister = new EventRegisterEntity(
                null,
                user.id(),
                eventById
        );
        eventRegisterRepository.save(gotRegister);
    }


    public void cancelRegistration(User user, Long eventId) {
        var event = eventService.getEventById(eventId);
        var findRegister = eventRegisterRepository.findByEventIdAndUserId(user.id(), eventId);
        if (findRegister.isEmpty()) {
           throw new IllegalArgumentException("Register to event does not exist");
        }
        if (!event.status().equals(EventStatus.WAIT_START)) {
            throw new IllegalArgumentException("Event has started or finished");
        }

        eventRegisterRepository.delete(findRegister.orElseThrow());
    }


    public List<Event> findRegEvents(Long userId) {
        var findEvents = eventRegisterRepository.findEvents(userId);
        return findEvents.stream()
                .map(eventEntityConverter::toDomain)
                .toList();
    }
}
