package com.kleim.eventmanager.controller;

import com.kleim.eventmanager.model.event.EventConverter;
import com.kleim.eventmanager.model.event.EventDto;
import com.kleim.eventmanager.model.event.EventRequestDto;
import com.kleim.eventmanager.service.EventService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
