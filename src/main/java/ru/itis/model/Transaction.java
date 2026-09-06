package ru.itis.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "source_contract_id", nullable = false)
    private UUID sourceContractId;

    @Column(name = "target_contract_id", nullable = false)
    private UUID targetContractId;

    @Column(name = "source_user_id", nullable = false)
    private UUID sourceUserId;

    @Column(name = "target_user_id", nullable = false)
    private UUID targetUserId;

    @Column(nullable = false)
    private BigDecimal amount;

    private String description;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
