package ru.kpfu.itis.repository;

import ru.kpfu.itis.model.Contract;

import java.math.BigDecimal;
import java.util.Optional;

public interface ContractRepository {

    Optional<Contract> findByContractName(String name);

    Contract save(Contract contract);

    Contract update(Contract contract);

    BigDecimal getBalanceByContractName(String name);

}
