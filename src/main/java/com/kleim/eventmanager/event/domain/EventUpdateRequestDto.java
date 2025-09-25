package com.kleim.eventmanager.event.domain;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record EventUpdateRequestDto(

        String name,

        @Positive
        Integer maxPlace,

        @Future
        LocalDateTime date,

        @Positive
        Integer cost,

        @Positive
        Integer duration,

        Long locationId
) {
}
