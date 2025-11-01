package ru.kpfu.itis.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private String timestamp = Instant.now().toString();

    public ErrorResponse(String message) {
        this.message = message;
    }
}