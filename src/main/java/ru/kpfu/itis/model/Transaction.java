package ru.kpfu.itis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    private UUID id;

    private UUID sourceContractId;

    private UUID targetContractId;

    private BigDecimal amount;

    private String description;

    private Instant createdAt;
}