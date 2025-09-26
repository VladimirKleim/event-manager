package com.kleim.eventmanager.serverdto;


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

    private final Logger exceptionLogger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ServerErrorDTO> handleValidationException(
            MethodArgumentNotValidException e
    ) {
        exceptionLogger.info("Not valid result");
        String detailMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(it -> it.getField() + " " + it.getDefaultMessage())
                .collect(Collectors.joining(", "));

        var errorDTO = new ServerErrorDTO(
                "Not valid result",
                detailMessage,
                LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ServerErrorDTO> handleEntityNotFoundException(
            EntityNotFoundException e
    ) {
        exceptionLogger.info("Not valid result : Entity no found");
        var errorDTO = new ServerErrorDTO(
                "todo",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }

    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class})
    public ResponseEntity<ServerErrorDTO> handleIllegalArgumentException(
            IllegalArgumentException e
    ) {
        exceptionLogger.error("Not valid result : Bad request");
        var errorDTO = new ServerErrorDTO("Illegal argument",
                e.getMessage(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServerErrorDTO> handleGenericException(
            Exception e
    ) {
        exceptionLogger.error("Got exception", e);
        var errorDTO = new ServerErrorDTO("Ошибка на сервере",
                e.getMessage(),
                LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredential(
            BadCredentialsException e
    ) {
        exceptionLogger.error("Got bad credential exception", e);
        var errorDTO = new ServerErrorDTO("bad credential",
                e.getMessage(),
                LocalDateTime.now());

        return ResponseEntity.status(401).body(errorDTO);
    }

}




