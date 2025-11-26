package com.kleim.eventmanager.event.api;

import com.kleim.eventmanager.auth.domain.AuthenticationService;
import com.kleim.eventmanager.event.domain.EventConverter;
import com.kleim.eventmanager.event.domain.EventDto;
import com.kleim.eventmanager.event.domain.EventRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events/registrations")
public class EventRegistrationController {

    private final Logger log = LoggerFactory.getLogger(EventRegistrationController.class);
    private final EventRegistrationService registrationService;
    private final AuthenticationService authenticationService;
    private final EventConverter eventConverter;


    public EventRegistrationController(EventRegistrationService registrationService, AuthenticationService authenticationService, EventConverter eventConverter) {
        this.registrationService = registrationService;
        this.authenticationService = authenticationService;
        this.eventConverter = eventConverter;
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<Void> registrationForEvent (
            @PathVariable("eventId") Long eventId
    ) {
        log.info("Got request to register to event with id: {}", eventId);
        var user = authenticationService.getCurrentAuthUser();
        registrationService.registerToEvent(eventId, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/cancel/{eventId}")
    public ResponseEntity<Void> deleteRegistration(
            @PathVariable("eventId") Long eventId
    ) {
        log.info("Got request to cancel registration to event");
        registrationService.cancelRegistration(
                authenticationService.getCurrentAuthUser(),
                eventId
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> findRegisteredEvents(
    ) {
        var findAll = registrationService.findRegEvents(authenticationService.getCurrentAuthUser().id());
        return ResponseEntity.ok().body(
                findAll.stream()
                        .map(eventConverter::toDto)
                        .toList()
        );
    }

}









