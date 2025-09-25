package com.kleim.eventmanager.event.domain;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record EventRequestDto(
        @NotBlank
        String name,
        @Positive
        int maxPlace,
        @Future
        LocalDateTime date,
        @NotNull
        int cost,
        @NotNull
        int duration,
        @NotNull
        Long locationId
) {
}
