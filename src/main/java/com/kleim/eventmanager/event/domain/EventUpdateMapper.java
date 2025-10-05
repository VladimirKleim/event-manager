package com.kleim.eventmanager.event.domain;

import org.springframework.stereotype.Component;

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

    public Event updateEventFields(Event event, EventUpdateRequest updateRequest) {
        return new Event(
                event.id(),
                Optional.ofNullable(updateRequest.name()).orElse(event.name()),
                event.ownerId(),
                Optional.ofNullable(updateRequest.maxPlace()).orElse(event.maxPlace()),
                event.registrationList(),
                Optional.ofNullable(updateRequest.date()).orElse(event.date()),
                Optional.ofNullable(updateRequest.cost()).orElse(event.cost()),
                Optional.ofNullable(updateRequest.duration()).orElse(event.duration()),
                Optional.ofNullable(updateRequest.locationId()).orElse(event.locationId()),
                event.status()
        );
    }

}
