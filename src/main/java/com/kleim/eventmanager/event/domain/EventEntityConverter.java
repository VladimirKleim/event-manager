package com.kleim.eventmanager.event.domain;

import com.kleim.eventmanager.event.db.EventEntity;
import com.kleim.eventmanager.event.db.EventRegisterEntity;
import org.springframework.stereotype.Component;

@Component
public class EventEntityConverter {

    public Event toDomain(EventEntity eventEntity) {
        return new Event(
                eventEntity.getId(),
                eventEntity.getName(),
                eventEntity.getOwnerId(),
                eventEntity.getMaxPlace(),
                eventEntity.getRegistrationList().stream().map(it ->
                                new EventRegistration(
                                        it.getId(),
                                        it.getUserId(),
                                        eventEntity.getId())
                        ).toList(),
                eventEntity.getDate(),
                eventEntity.getCost(),
                eventEntity.getDuration(),
                eventEntity.getLocationId(),
                eventEntity.getStatus()
        );
    }


    public EventEntity toEntity(Event event) {
        return new EventEntity(
                event.id(),
                event.name(),
                event.ownerId(),
                event.maxPlace(),
                event.registrationList().stream().map(it ->
                        new EventRegisterEntity(
                                it.id(),
                                it.userId(),
                                toEntity(event))
                ).toList(),
                event.date(),
                event.cost(),
                event.duration(),
                event.locationId(),
                event.status()
        );
    }
}
