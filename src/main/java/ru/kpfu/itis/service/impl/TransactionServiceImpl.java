package ru.kpfu.itis.service.impl;

import lombok.RequiredArgsConstructor;
import ru.kpfu.itis.model.Transaction;
import ru.kpfu.itis.repository.TransactionRepository;
import ru.kpfu.itis.service.TransactionService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public Transaction create(UUID sourceContractId, UUID targetContractId, BigDecimal amount, String description) {
        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .sourceContractId(sourceContractId)
                .targetContractId(targetContractId)
                .amount(amount)
                .description(description)
                .createdAt(Instant.now())
                .build();

        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction getTransactionById(UUID transactionId) {
        return transactionRepository.findById(transactionId).orElse(null);
    }

    @Override
    public List<Transaction> getTransactionsByContractId(UUID sourceContractId) {
        return transactionRepository.findByContractId(sourceContractId);
    }

    @Override
    public List<Transaction> getTransactionsByContractName(String contractName) {
        return transactionRepository.getTransactionsByContractName(contractName);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
