package com.kleim.eventmanager.event.domain;

import jakarta.persistence.MapsId;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record EventUpdateRequestDto(

        String name,

        @Positive
        @Min(30)
        Integer maxPlace,

        @Future
        LocalDateTime date,

        @Positive
        Integer cost,

        @Positive
        @Min(30)
        Integer duration,

        Long locationId
) {
}
