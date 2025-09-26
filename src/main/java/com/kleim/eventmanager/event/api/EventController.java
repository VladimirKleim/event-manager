package com.kleim.eventmanager.event.api;

import com.kleim.eventmanager.event.domain.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
      log.info("Got request to create and save event with name:{}", eventDto.name());
      var savedEvent = eventService.eventCreate(eventDto);
      return ResponseEntity.status(HttpStatus.CREATED).body(eventConverter.toDto(savedEvent));
    }


    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEventById(
            @PathVariable("eventId") Long eventId
    ) {
        log.info("Got request get event with id:{}", eventId);
        var gotEvent = eventService.getEventById(eventId);

        return ResponseEntity.status(HttpStatus.OK).body(eventConverter.toDto(gotEvent));
    }


    @GetMapping
    public ResponseEntity<List<EventDto>> getAllEvents() {
        log.info("Got request to get all events");
        var gotEvents = eventService.getAllEvents();
        return ResponseEntity.ok()
                .body(gotEvents.stream()
                        .map(eventConverter::toDto)
                        .toList());
    }


    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> eventSoftDelete(
            @PathVariable("eventId") Long eventId
    ) {
        log.info("Got request to cancel event with id:{}", eventId);
        eventService.cancelEventById(eventId);
       return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PutMapping("/{eventId}")
    public ResponseEntity<EventDto> updateEvent(
            @PathVariable("eventId") Long eventId,
            @RequestBody @Valid EventUpdateRequestDto updateRequestDto
    ) {
        log.info("Got request to update");
        var updatedEvent = eventService.updateEvent(eventId, updateRequestDto);
        return ResponseEntity.ok().body(eventConverter.toDto(updatedEvent));
    }

    @PostMapping("/search")
    public ResponseEntity<List<EventDto>> eventSearchFilter(
            @RequestBody EventSearchRequestDto searchRequestDto
    ) {
        log.info("Got request to search event by filter");
        var searchEvents = eventService.searchFilter(searchRequestDto);
        return ResponseEntity.ok().body(
                searchEvents.stream()
                .map(eventConverter::toDto)
                .toList()
        );
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getOwnerEvent() {
        log.info("Got request to get their events");
        var gotEvent = eventService.getOwner();
        return ResponseEntity.ok().body(gotEvent.stream().map(eventConverter::toDto).toList());
    }

}
