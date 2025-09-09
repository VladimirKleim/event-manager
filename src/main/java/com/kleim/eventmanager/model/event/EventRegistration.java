package com.kleim.eventmanager.model.event;

public record EventRegistration(
        Long id,
        Long userId,
        Long eventId
) {
}
