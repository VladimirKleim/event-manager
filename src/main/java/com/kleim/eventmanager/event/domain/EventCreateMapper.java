package com.kleim.eventmanager.event.domain;

import org.springframework.stereotype.Component;

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
}
