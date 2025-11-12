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
    public Transaction create(UUID fromContractId, UUID toContractId, BigDecimal amount, String description) {
        Contract fromContract = contractRepository.findById(fromContractId)
                .orElseThrow(() -> new RuntimeException("Source contract not found"));
        Contract toContract = contractRepository.findById(toContractId)
                .orElseThrow(() -> new RuntimeException("Target contract not found"));

        if (fromContract.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        fromContract.setBalance(fromContract.getBalance().subtract(amount));
        toContract.setBalance(toContract.getBalance().add(amount));

        contractRepository.update(fromContract);
        contractRepository.update(toContract);

        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .sourceContractId(fromContractId)
                .targetContractId(toContractId)
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
    public List<Transaction> getTransactionsByContractId(UUID contractId) {
        return transactionRepository.findByContractId(contractId);
    }

    @Override
    public List<Transaction> getTransactionsByContractName(String contractName) {
        return transactionRepository.findByContractName(contractName);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}