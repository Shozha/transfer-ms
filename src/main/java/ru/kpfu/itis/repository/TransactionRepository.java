package ru.kpfu.itis.repository;

import ru.kpfu.itis.model.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    Transaction save(Transaction transaction);
    Optional<Transaction> findById(UUID id);
    List<Transaction> findByContractId(UUID contractId);
    List<Transaction> findAll();
    List<Transaction> getTransactionsByContractName(String contractName);
}
