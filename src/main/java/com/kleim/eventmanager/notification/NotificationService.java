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

import java.util.List;
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

        var eventKafka = new NotificationEvent();

        eventKafka.setEventId(event.getId());
        eventKafka.setOwnerId(event.getOwnerId());
        eventKafka.setChangedById(userId);
        eventKafka.setStatus(new FieldChange<>(event.getStatus(), eventStatus));

        kafkaProducer.sendMessage(eventKafka);
    }

    public void changeAllFieldsWhenUpdate(EventUpdateRequest eventUpdateRequest, EventEntity eventEntity, List<String> logins) {
        NotificationEvent notification = new NotificationEvent();
        notification.setEventId(eventEntity.getId());
        notification.setSubscribersLogins(logins);
        notification.setOwnerId(eventEntity.getOwnerId());
        notification.setChangedById(authenticationService.getCurrentAuthUser().id());


        Optional.ofNullable(eventUpdateRequest.name()).filter(e -> !e.equals(eventEntity.getName()))
                .ifPresent(e -> notification.setName(new FieldChange<>(eventEntity.getName(), e)));
        Optional.ofNullable(eventUpdateRequest.maxPlace()).filter(e -> !e.equals(eventEntity.getMaxPlace()))
                .ifPresent(e -> notification.setMaxPlaces(new FieldChange<>(eventEntity.getMaxPlace(), e)));
        Optional.ofNullable(eventUpdateRequest.date()).filter(e -> !e.equals(eventEntity.getDate()))
                .ifPresent(e -> notification.setDate(new FieldChange<>(eventEntity.getDate(), e)));
        Optional.ofNullable(eventUpdateRequest.cost()).filter(e -> !e.equals(eventEntity.getCost()))
                .ifPresent(e -> notification.setCost(new FieldChange<>(eventEntity.getCost(), e)));
        Optional.ofNullable(eventUpdateRequest.duration()).filter(e -> !e.equals(eventEntity.getDuration()))
                .ifPresent(e -> notification.setDuration(new FieldChange<>(eventEntity.getDuration(), e)));
        Optional.ofNullable(eventUpdateRequest.locationId()).filter(e -> !e.equals(eventEntity.getLocationId()))
                .ifPresent(e -> notification.setLocationId(new FieldChange<>(eventEntity.getLocationId(), e)));

        kafkaProducer.sendMessage(notification);
    }
}
