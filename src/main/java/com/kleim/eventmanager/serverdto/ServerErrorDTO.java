package com.kleim.eventmanager.serverdto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;


public record ServerErrorDTO(

        String message,
        String detailMessage,
        LocalDateTime localDateTime

) {}
