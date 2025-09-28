package com.kleim.eventmanager.event.domain;

import org.springframework.stereotype.Component;

@Component
public class EventUpdateMapper {

    public EventUpdateRequest toDomain (EventUpdateRequestDto eventUpdateRequest) {
        return new EventUpdateRequest(
                eventUpdateRequest.name(),
                eventUpdateRequest.maxPlace(),
                eventUpdateRequest.date(),
                eventUpdateRequest.cost(),
                eventUpdateRequest.duration(),
                eventUpdateRequest.locationId()
        );
    }

}
