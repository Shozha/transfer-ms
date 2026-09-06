package ru.itis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.mapper.TransactionMapper;
import ru.itis.dto.request.TransactionRequest;
import ru.itis.dto.response.TransactionElementResponse;
import ru.itis.dto.response.TransactionResponse;
import ru.itis.model.Contract;
import ru.itis.model.Transaction;
import ru.itis.service.TransactionService;

import java.time.Instant;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transfers")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @GetMapping("/transactions/{name}")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable("name") String name) {
        log.info("Received request to get transactions for contract name: {}", name);
        List<Transaction> transactions = transactionService.findAllTransactionsByContractName(name);
        Instant now = Instant.now();
        log.debug("Returning transaction response with {} transactions", transactions.size());
        return ResponseEntity.ok(
                transactionMapper.toResponse(
                        name,
                        now,
                        now,
                        transactions
                )
        );
    }

    @PostMapping("/transactions")
    public ResponseEntity<TransactionElementResponse> createTransaction(@RequestBody TransactionRequest request) {
        log.info("Received request to create transaction: {}", request);
        Transaction transaction = transactionService.save(
                request.getSourceContractName(),
                request.getTargetContractName(),
                request.getAmount(),
                request.getDescription()
        );
        log.info("Transaction created successfully with id: {}", transaction.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionMapper.toElementResponse(transaction)) ;
    }
}
