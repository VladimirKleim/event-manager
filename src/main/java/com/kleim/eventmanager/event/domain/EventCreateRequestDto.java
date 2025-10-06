package com.kleim.eventmanager.event.domain;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record EventCreateRequestDto(
        @NotBlank
        String name,
        @Positive
        Integer maxPlace,
        @Future
        LocalDateTime date,
        @NotNull
        @PositiveOrZero
        Integer cost,
        @NotNull
        @Min(30)
        Integer duration,
        @NotNull
        Long locationId
) {
}
