package ru.itis.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionElementResponse {
    //UUID
    private String sourceUserId;
    //UUID
    private String targetUserId;

    private BigDecimal amount;

    private String description;

    private String createdAt;

}
