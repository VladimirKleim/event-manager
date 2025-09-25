package com.kleim.eventmanager.event.domain;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record EventDto(
        Long id,
        @NotBlank
        String name,
        Long ownerId,
        @Positive
        Integer maxPlace,
        Integer occupiedPlace,
        @Future
        LocalDateTime date,
        @NotNull
        Integer cost,
        @NotNull
        Integer duration,
        @NotNull
        Long locationId,
        EventStatus status
)
{
}
