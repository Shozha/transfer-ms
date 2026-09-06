package ru.itis.mapper;

import org.springframework.stereotype.Component;
import ru.itis.dto.response.TransactionElementResponse;
import ru.itis.dto.response.TransactionResponse;
import ru.itis.model.Transaction;

import java.time.Instant;
import java.util.List;

@Component
public class TransactionMapper {

    public TransactionElementResponse toElementResponse(Transaction transaction) {
        return TransactionElementResponse.builder()
                .sourceUserId(transaction.getSourceUserId().toString())
                .targetUserId(transaction.getTargetUserId().toString())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .createdAt(transaction.getCreatedAt().toString())
                .build();
    }

    public TransactionResponse toResponse(String contractName,
                                          Instant from,
                                          Instant to,
                                          List<Transaction> transactions) {
        List<TransactionElementResponse> list = transactions.stream()
                .map(this::toElementResponse)
                .toList();

        return TransactionResponse.builder()
                .contractName(contractName)
                .from(from.toString())
                .to(to.toString())
                .transactions(list)
                .build();
    }
}
