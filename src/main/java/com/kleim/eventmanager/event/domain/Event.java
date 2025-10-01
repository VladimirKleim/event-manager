package com.kleim.eventmanager.event.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public record Event(
        Long id,

        String name,

        Long ownerId,

        Integer maxPlace,

        List<EventRegistration> registrationList,

        LocalDateTime date,

        Integer cost,

        Integer duration,

        Long locationId,

        EventStatus status
){}



