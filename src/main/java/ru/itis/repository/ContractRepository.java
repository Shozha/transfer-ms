package ru.itis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.model.Contract;

import java.util.Optional;
import java.util.UUID;

public interface ContractRepository extends JpaRepository<Contract, UUID> {

    Optional<Contract> findContractByContractName(String contractName);
}
