package ru.kpfu.itis.service.impl;

import lombok.RequiredArgsConstructor;
import ru.kpfu.itis.model.Contract;
import ru.kpfu.itis.model.Transaction;
import ru.kpfu.itis.repository.ContractRepository;
import ru.kpfu.itis.repository.TransactionRepository;
import ru.kpfu.itis.service.TransactionService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ContractRepository contractRepository;

    @Override
    public Transaction create(UUID sourceContractId, UUID targetContractId, BigDecimal amount, String description) {
        validateCreateRequest(sourceContractId, targetContractId, amount);

        if (sourceContractId.equals(targetContractId)) {
            throw new IllegalArgumentException("Source and target contracts cannot be the same");
        }

        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .sourceContractId(fromContractId)
                .targetContractId(toContractId)
                .amount(amount)
                .description(description != null ? description.trim() : "")
                .createdAt(Instant.now())
                .build();

        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction getTransactionById(UUID transactionId) {
        return transactionRepository.findById(transactionId).orElse(null);
    }

    @Override
    public List<Transaction> getTransactionsByContractId(UUID contractId) {
        return transactionRepository.findByContractId(contractId);
    }

    @Override
    public List<Transaction> getTransactionsByContractName(String contractName) {
        if (contractName == null || contractName.trim().isEmpty()) {
            throw new IllegalArgumentException("Contract name cannot be empty");
        }
        return transactionRepository.getTransactionsByContractName(contractName.trim());
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    private void validateCreateRequest(UUID sourceContractId, UUID targetContractId, BigDecimal amount) {
        if (sourceContractId == null) {
            throw new IllegalArgumentException("Source contract ID cannot be null");
        }
        if (targetContractId == null) {
            throw new IllegalArgumentException("Target contract ID cannot be null");
        }
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }
}
