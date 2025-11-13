package ru.kpfu.itis.repository;

import ru.kpfu.itis.model.Contract;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface ContractRepository {

    Optional<Contract> findById(UUID id);

    Optional<Contract> findByContractName(String name);

    Contract save(Contract contract);

    Contract update(Contract contract);

    BigDecimal getBalanceByContractName(String name);

}