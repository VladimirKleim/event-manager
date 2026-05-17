package com.kleim.eventmanager.middleware;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ServerErrorMessage> handleValidationException(
            MethodArgumentNotValidException e
    ) {
        logger.info("Not valid result");
        String detailMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(it -> it.getField() + " " + it.getDefaultMessage())
                .collect(Collectors.joining(", "));

        var errorDTO = new ServerErrorMessage(
                "Not valid result",
                detailMessage,
                LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ServerErrorMessage> handleEntityNotFoundException(
            EntityNotFoundException e
    ) {
        logger.info("Not valid result : Entity no found");
        var errorDTO = new ServerErrorMessage(
                "todo",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }

    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class})
    public ResponseEntity<ServerErrorMessage> handleIllegalArgumentException(
            IllegalArgumentException e
    ) {
        logger.error("Not valid result : Bad request");
        var errorDTO = new ServerErrorMessage("Illegal argument",
                e.getMessage(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServerErrorMessage> handleGenericException(
            Exception e
    ) {
        logger.error("Got exception", e);
        var errorDTO = new ServerErrorMessage("Ошибка на сервере",
                e.getMessage(),
                LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredential(
            BadCredentialsException e
    ) {
        logger.error("Got bad credential exception", e);
        var errorDTO = new ServerErrorMessage("bad credential",
                e.getMessage(),
                LocalDateTime.now());

        return ResponseEntity.status(401).body(errorDTO);
    }
}
