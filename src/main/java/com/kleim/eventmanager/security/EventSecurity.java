package com.kleim.eventmanager.security;

import com.kleim.eventmanager.auth.domain.AuthenticationService;
import com.kleim.eventmanager.auth.domain.UserRole;
import com.kleim.eventmanager.event.db.EventRepository;
import com.kleim.eventmanager.event.domain.Event;
import com.kleim.eventmanager.mapper.EventDbMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("eventSecurity")
public class EventSecurity {

    private final AuthenticationService authenticationService;
    private final EventRepository eventRepository;
    @Autowired
    private EventDbMapper eventDbMapper;

    public EventSecurity(AuthenticationService authenticationService, EventRepository eventRepository) {
        this.authenticationService = authenticationService;
        this.eventRepository = eventRepository;
    }

    public boolean canModified(Long eventId) {
        var user = authenticationService.getCurrentAuthUser();
        var eventDb =  eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event with id %s does not exists"));
        var event = new Event(
                eventDb.getId(),
                eventDb.getName(),
                eventDb.getOwnerId(),
                eventDb.getMaxPlace(),
                null,
                eventDb.getDate(),
                eventDb.getCost(),
                eventDb.getDuration(),
                eventDb.getLocationId(),
                eventDb.getStatus()
        );
        if (event.ownerId().equals(user.id()) || user.role().equals(UserRole.ADMIN)) {
            return true;
        }
        return false;
    }
}
