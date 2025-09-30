package com.kleim.eventmanager.event.domain;

import com.kleim.eventmanager.auth.domain.AuthenticationService;
import com.kleim.eventmanager.event.EventChangeKafkaMessage;
import com.kleim.eventmanager.event.FieldChange;
import com.kleim.eventmanager.event.db.EventEntity;
import com.kleim.eventmanager.event.db.EventRepository;
import com.kleim.eventmanager.kafka.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

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
        log.info("Get message for updated event: {}, updated status: {}",eventId, eventStatus);
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
            Long eventId,
            EventUpdateRequest eventUpdateRequest
    ) {
      log.info("");

      var event = eventRepository.findById(eventId).orElseThrow(() ->
          new NoSuchElementException("Event with id %s not exist".formatted(eventId)));

      var userId = authenticationService.getCurrentAuthUser().id();

      var eventKafka = new EventChangeKafkaMessage();

      eventKafka.setEventId(eventId);
      eventKafka.setOwnerId(event.getOwnerId());
      eventKafka.setChangedById(userId);


    }

}
