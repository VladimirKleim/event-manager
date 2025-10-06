package com.kleim.eventmanager.event.domain;

import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

@Component
public class EventSearchMapper {

    public EventSearchRequest toDomain(EventSearchRequestDto searchRequest) {
        return new EventSearchRequest(
                searchRequest.name(),
                searchRequest.placesMin(),
                searchRequest.placesMax(),
                searchRequest.dateStartAfter(),
                searchRequest.dateStartBefore(),
                searchRequest.costMin(),
                searchRequest.costMax(),
                searchRequest.durationMin(),
                searchRequest.durationMax(),
                searchRequest.locationId(),
                searchRequest.eventStatus()
        );
    }
}
