package ru.itis.controller.handler;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionMessage {

    private LocalDateTime time;

    private String exceptionName;

    private String message;
}
