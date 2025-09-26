package com.kleim.eventmanager.location.domain;

import jakarta.validation.constraints.*;

public record LocationDto(
        @Null
        Long id,

        @NotBlank
        @Size(max=55)
        String name,

        @NotBlank
        String address,

        @NotNull
        @Min(5)
        Integer capacity,

        String description

) {}
