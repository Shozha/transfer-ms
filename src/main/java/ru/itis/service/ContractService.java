package ru.itis.service;

import ru.itis.model.Contract;

import java.math.BigDecimal;
import java.util.UUID;

public interface ContractService {

    Contract getById(UUID id);

    Contract getByContractName(String name);

    BigDecimal getBalanceByContractName(String name);

    void updateContractName(UUID id, Contract contract);

    void processTransfer(Contract source, Contract target, BigDecimal amount);

    Contract createEmptyContract();
}
