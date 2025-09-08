package com.kleim.eventmanager.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kleim.eventmanager.serverdto.ServerErrorDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.annotations.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

    private final Logger logger = LoggerFactory.getLogger(AccessDeniedHandler.class);
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
            logger.info("Handle access denied exception ");
            var messageResponse = new ServerErrorDTO(
                    "Got error to access",
                    accessDeniedException.getMessage(),
                    LocalDateTime.now()
            );

            var stringResponse = objectMapper.writeValueAsString(messageResponse);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write(stringResponse);

    }
}
