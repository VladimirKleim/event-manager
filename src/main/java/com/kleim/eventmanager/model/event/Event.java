package com.kleim.eventmanager.model.event;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.List;

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
) {
}
