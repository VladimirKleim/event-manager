package com.kleim.eventmanager.event.domain;

import jakarta.validation.constraints.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


public record EventCreateRequest(

        String name,

        Integer maxPlace,

        LocalDateTime date,

        Integer cost,

        Integer duration,

        Long locationId
) {


}
