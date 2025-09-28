package com.kleim.eventmanager.event.domain;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record EventUpdateRequest(

        String name,

        Integer maxPlace,

        LocalDateTime date,

        Integer cost,

        Integer duration,

        Long locationId
) {
}
