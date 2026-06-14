package com.kleim.eventmanager.event.domain;

import java.time.LocalDateTime;

public record EventCreateRequest(

        String name,

        Integer maxPlace,

        LocalDateTime date,

        Integer cost,

        Integer duration,

        Long locationId

) {}
