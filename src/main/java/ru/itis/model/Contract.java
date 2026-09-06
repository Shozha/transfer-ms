package ru.itis.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "contracts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "contract_name", nullable = false, unique = true)
    private String contractName;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;
}
