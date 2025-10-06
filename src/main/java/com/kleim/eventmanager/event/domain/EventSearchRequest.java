package com.kleim.eventmanager.event.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventSearchRequest(
        String name,
        Integer placesMin,
        Integer placesMax,
        LocalDateTime dateStartAfter,
        LocalDateTime dateStartBefore,
        BigDecimal costMin,
        BigDecimal costMax,
        Integer durationMin,
        Integer durationMax,
        Long locationId,
        EventStatus eventStatus
) {
}
