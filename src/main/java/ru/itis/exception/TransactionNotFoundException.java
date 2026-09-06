package ru.itis.exception;

import java.util.UUID;

public class TransactionNotFoundException extends NotFoundException {

    public TransactionNotFoundException(UUID id) {
        super(String.format("Transaction with id = %s not found", id));
    }
}
