package com.kleim.eventmanager.serverdto;

import java.time.LocalDateTime;


public record ServerErrorDTO(

        String message,
        String detailMessage,
        LocalDateTime localDateTime

) {}
