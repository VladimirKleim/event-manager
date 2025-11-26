package com.kleim.eventmanager.event.domain;

import com.kleim.eventmanager.event.db.EventEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventCreateMapper {

    public EventCreateRequest toDomain(EventCreateRequestDto eventCreateRequest) {
        return new EventCreateRequest(
                eventCreateRequest.name(),
                eventCreateRequest.maxPlace(),
                eventCreateRequest.date(),
                eventCreateRequest.cost(),
                eventCreateRequest.duration(),
                eventCreateRequest.locationId()
        );
    }

    public EventEntity toEntity(Long userId, EventCreateRequest eventCreateRequest) {
        return new EventEntity(
                null,
                eventCreateRequest.name(),
                userId,
                eventCreateRequest.maxPlace(),
                List.of(),
                eventCreateRequest.date(),
                eventCreateRequest.cost(),
                eventCreateRequest.duration(),
                eventCreateRequest.locationId(),
                EventStatus.WAIT_START
        );
    }
}
