package ru.kpfu.itis.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kpfu.itis.model.Transaction;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private String id;
    private String sourceContractId;
    private String targetContractId;
    private BigDecimal amount;
    private String description;
    private Instant createdAt;

    public static TransactionResponse fromTransaction(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        return new TransactionResponse(
                transaction.getId().toString(),
                transaction.getSourceContractId().toString(),
                transaction.getTargetContractId().toString(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getCreatedAt()
        );
    }
}
