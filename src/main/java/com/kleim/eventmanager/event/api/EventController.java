package com.kleim.eventmanager.event.api;

import com.kleim.eventmanager.event.domain.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
public class EventController {
    private final Logger log = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;

    private final EventConverter eventConverter;

    public EventController(EventService eventService, EventConverter eventConverter) {
        this.eventService = eventService;
        this.eventConverter = eventConverter;
    }


    @PostMapping
    public ResponseEntity<EventDto> createEvent(
            @RequestBody @Valid EventRequestDto eventDto
    ) {
      log.info("");
      var savedEvent = eventService.eventCreate(eventDto);
      return ResponseEntity.status(HttpStatus.CREATED).body(eventConverter.toDto(savedEvent));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEventById(
            @PathVariable("eventId") Long eventId
    ) {
        var foundEvent = eventService.findEventById(eventId);
        return ResponseEntity.status(HttpStatus.FOUND).body(eventConverter.toDto(foundEvent));

    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEventById(
           @PathVariable("eventId") Long eventId
    ) {
        log.info("");
        eventService.deleteEvent(eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventDto> updateEvent(
            @PathVariable("eventId") Long eventId,
            @RequestBody @Valid EventUpdateRequestDto update
    ) {
       var updatedEvent = eventService.eventUpdate(eventId, update);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(eventConverter.toDto(updatedEvent));
    }
}
