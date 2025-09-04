package com.kleim.eventmanager.serverdto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;


//@JsonInclude(content = JsonInclude.Include.NON_EMPTY)
public record ServerErrorDTO(

        String message,
        String detailMessage,
        LocalDateTime localDateTime

) {}
