package com.kleim.eventmanager.middleware;

import java.time.LocalDateTime;


public record ServerErrorDTO(

        String message,
        String detailMessage,
        LocalDateTime localDateTime

) {}
