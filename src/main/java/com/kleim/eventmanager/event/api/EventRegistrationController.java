package com.kleim.eventmanager.event.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events/registration")
public class EventRegistrationController {

    private final Logger log = LoggerFactory.getLogger(EventRegistrationController.class);

    @PostMapping("/{eventId}")
    public ResponseEntity<Void> registrationForEvent (
            @PathVariable("eventId") Long eventId
    ) {
        log.info("Got request to register to event with id: {}", eventId);
    }
}
