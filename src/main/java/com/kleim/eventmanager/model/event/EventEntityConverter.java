package com.kleim.eventmanager.model.event;

import com.kleim.eventmanager.entity.EventEntity;
import com.kleim.eventmanager.entity.EventRegisterEntity;
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

}
