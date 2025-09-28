package com.kleim.eventmanager.event.domain;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record EventCreateRequestDto(
        @NotBlank
        String name,
        @Positive
        int maxPlace,
        @Future
        LocalDateTime date,
        @NotNull
        @PositiveOrZero
        int cost,
        @NotNull
        @Min(30)
        int duration,
        @NotNull
        Long locationId
) {
}
