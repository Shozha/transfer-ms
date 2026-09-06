package ru.itis.exception;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class InsufficientFundsException extends ServiceException {

    public InsufficientFundsException(UUID contractId) {
        super(String.format("Not enough money on source contract: %s", contractId), HttpStatus.BAD_REQUEST);
    }
}
