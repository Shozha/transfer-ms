package ru.itis.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UserBlockedException extends ServiceException {

    public UserBlockedException(UUID userId) {
        super(String.format("Transfer is not allowed: user %s is blocked", userId), HttpStatus.FORBIDDEN);
    }
}
