package com.kleim.eventmanager.middleware;

import java.time.LocalDateTime;

public record ServerErrorMessage(

        String message,
        String detailMessage,
        LocalDateTime localDateTime

) {}
