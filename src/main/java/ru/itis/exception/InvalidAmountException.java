package ru.itis.exception;

import org.springframework.http.HttpStatus;

public class InvalidAmountException extends ServiceException {

    public InvalidAmountException() {
        super("Transfer amount must be positive", HttpStatus.BAD_REQUEST);
    }
}
