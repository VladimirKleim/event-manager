package com.kleim.eventmanager.event.domain;

import jakarta.validation.constraints.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


public record EventCreateRequest(

        String name,

        int maxPlace,

        LocalDateTime date,

        int cost,

        int duration,

        Long locationId
) {


}
