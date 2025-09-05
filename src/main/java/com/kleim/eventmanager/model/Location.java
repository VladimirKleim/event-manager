package com.kleim.eventmanager.model;

public record Location(

        Long id,
        String name,
        String address,
        Integer capacity,
        String description

) {
}
