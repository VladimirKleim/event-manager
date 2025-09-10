package com.kleim.eventmanager.model.event;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record EventUpdateRequestDto(
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
