package com.kleim.eventmanager.event.domain;

import com.kleim.eventmanager.event.db.EventEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EventUpdateMapper {

    public EventUpdateRequest toDomain(EventUpdateRequestDto eventUpdateRequest) {
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
