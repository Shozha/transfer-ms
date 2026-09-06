package ru.itis.exception;

public class ContractNotFoundException extends NotFoundException {

    public ContractNotFoundException(String contractName) {
        super(String.format("Contract with name = %s not found", contractName));
    }

}
