package com.kleim.eventmanager.notification;

import com.kleim.eventmanager.auth.domain.AuthenticationService;
import com.kleim.eventmanager.event.db.EventEntity;
import com.kleim.eventmanager.event.db.EventRepository;
import com.kleim.eventmanager.event.domain.EventStatus;
import com.kleim.eventmanager.event.domain.EventUpdateRequest;
import com.kleim.eventmanager.kafka.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final KafkaProducer kafkaProducer;
    private final AuthenticationService authenticationService;

    private final EventRepository eventRepository;

    public NotificationService(KafkaProducer kafkaProducer, AuthenticationService authenticationService, EventRepository eventRepository) {
        this.kafkaProducer = kafkaProducer;
        this.authenticationService = authenticationService;
        this.eventRepository = eventRepository;
    }

    public void changeEventStatus(
            Long eventId,
            EventStatus eventStatus
    ) {
        log.info("Get message for update status: {}, updated status: {}",eventId, eventStatus);
        var event = eventRepository.findById(eventId).orElseThrow(() ->
                new NoSuchElementException("Event with id %s not exist".formatted(eventId)));

        var userId = authenticationService.getCurrentAuthUser().id();

        var eventKafka = new EventChangeKafkaMessage();

        eventKafka.setEventId(event.getId());
        eventKafka.setOwnerId(event.getOwnerId());
        eventKafka.setChangedById(userId);
        eventKafka.setStatus(new FieldChange<>(event.getStatus(), eventStatus));

        kafkaProducer.sendMessage(eventKafka);
    }

    public void ChangeAllEventsFields(
            EventUpdateRequest eventUpdateRequest,
            EventEntity event
    ) {
        log.info("Get message to update event: {}", event);


        var userId = authenticationService.getCurrentAuthUser().id();

        var eventKafka = new EventChangeKafkaMessage();

        eventKafka.setEventId(event.getId());
        eventKafka.setOwnerId(event.getOwnerId());
        eventKafka.setChangedById(userId);

        Optional.ofNullable(eventUpdateRequest.name()).filter(e -> !e.equals(event.getName()))
                .ifPresent(e -> eventKafka.setName(new FieldChange<>(event.getName(), e)));

        Optional.ofNullable(eventUpdateRequest.maxPlace()).filter(e -> !e.equals(event.getMaxPlace()))
                .ifPresent(e -> eventKafka.setMaxPlaces(new FieldChange<>(event.getMaxPlace(), e)));

        Optional.ofNullable(eventUpdateRequest.date()).filter(e -> !e.equals(event.getDate()))
                .ifPresent(e -> eventKafka.setDate(new FieldChange<>(event.getDate(), e)));

        Optional.ofNullable(eventUpdateRequest.cost()).filter(e -> !e.equals(event.getCost()))
                .ifPresent(e -> eventKafka.setCost(new FieldChange<>(event.getCost(), e)));

        Optional.ofNullable(eventUpdateRequest.duration()).filter(e -> !e.equals(event.getDuration()))
                .ifPresent(e -> eventKafka.setDuration(new FieldChange<>(event.getDuration(), e)));

        Optional.ofNullable(eventUpdateRequest.locationId()).filter(e -> e.equals(event.getLocationId()))
                .ifPresent(e -> eventKafka.setLocationId(new FieldChange<>(event.getLocationId(), e)));


        kafkaProducer.sendMessage(eventKafka);
    }
}
