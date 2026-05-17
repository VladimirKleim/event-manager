package com.kleim.eventmanager.event.api;

import com.kleim.eventmanager.event.domain.*;
import com.kleim.eventmanager.mapper.EventDbMapper;
import com.kleim.eventmanager.mapper.EventMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/events")
public class EventController implements EventApi {

    private final Logger log = LoggerFactory.getLogger(EventController.class);
    private final EventService eventService;
    private final EventMapper eventMapper;
    private final EventUpdateMapper eventUpdateMapper;
    private final EventDbMapper eventDbMapper;
    private final EventSearchMapper eventSearchMapper;

    public EventController(EventService eventService, EventMapper eventMapper, EventUpdateMapper eventUpdateMapper, EventDbMapper eventDbMapper, EventSearchMapper eventSearchMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
        this.eventUpdateMapper = eventUpdateMapper;
        this.eventDbMapper = eventDbMapper;
        this.eventSearchMapper = eventSearchMapper;
    }

    @PostMapping
    public ResponseEntity<EventDto> createEvent(
            @RequestBody @Valid EventCreateRequestDto eventDto
    ) {
      log.info("Got request to create and save event with name:{}", eventDto.name());
      var savedEvent = eventService.eventCreate(eventDbMapper.toDomain(eventDto));
      return ResponseEntity.status(HttpStatus.CREATED).body(eventMapper.toDto(savedEvent));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEventById(
            @PathVariable("eventId") Long eventId
    ) {
        log.info("Got request get event with id:{}", eventId);
        var gotEvent = eventService.getEventById(eventId);

        return ResponseEntity.status(HttpStatus.OK).body(eventMapper.toDto(gotEvent));
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getAllEvents() {
        log.info("Got request to get all events");
        var gotEvents = eventService.getAllEvents();
        return ResponseEntity.ok()
                .body(gotEvents.stream()
                        .map(eventMapper::toDto)
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
        var updatedEvent = eventService.updateEvent(eventId, eventUpdateMapper.toDomain(updateRequestDto));
        return ResponseEntity.ok().body(eventMapper.toDto(updatedEvent));
    }

    @PostMapping("/search")
    public ResponseEntity<List<EventDto>> eventSearchFilter(
            @RequestBody EventSearchRequestDto searchRequestDto
    ) {
        log.info("Got request to search event by filter");
        var searchEvents = eventService.searchFilter(eventSearchMapper.toDomain(searchRequestDto));
        return ResponseEntity.ok().body(
                searchEvents.stream()
                .map(eventMapper::toDto)
                .toList()
        );
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getOwnerEvent() {
        log.info("Got request to get their events");
        var gotEvent = eventService.getOwner();
        return ResponseEntity.ok().body(gotEvent.stream().map(eventMapper::toDto).toList());
    }

}
