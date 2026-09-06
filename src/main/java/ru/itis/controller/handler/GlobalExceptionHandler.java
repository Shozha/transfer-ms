package ru.itis.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import ru.itis.exception.ServiceException;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ExceptionMessage> handleServiceException(ServiceException ex) {
        log.warn("Business validation failed: {}", ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus())
                .body(ExceptionMessage.builder()
                        .time(LocalDateTime.now())
                        .exceptionName(ex.getClass().getSimpleName())
                        .message(ex.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ExceptionMessage> handleRestClientException(RestClientException ex) {
        log.warn("Rest client failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ExceptionMessage.builder()
                        .time(LocalDateTime.now())
                        .exceptionName(ex.getClass().getSimpleName())
                        .message(String.format("External service error: %s", ex.getMessage()))
                        .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionMessage> handleAllOtherExceptions(Exception ex) {
        log.error("Unexpected error occurred: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionMessage.builder()
                        .time(LocalDateTime.now())
                        .exceptionName(ex.getClass().getSimpleName())
                        .message(String.format("Internal server error: %s",  ex.getMessage()))
                        .build()
                );
    }
}
