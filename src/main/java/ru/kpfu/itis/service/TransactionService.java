package ru.kpfu.itis.service;

import ru.kpfu.itis.model.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
    Transaction create(UUID sourceContractId, UUID targetContractId, BigDecimal amount ,String description);
    Transaction getTransactionById(UUID transactionId);
    List<Transaction> getTransactionsByContractId(UUID sourceContractId);
    List<Transaction> getTransactionsByContractName(String contractName);
    List<Transaction> getAllTransactions();
}
