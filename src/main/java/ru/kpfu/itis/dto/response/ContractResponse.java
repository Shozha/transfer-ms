package ru.kpfu.itis.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kpfu.itis.model.Contract;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContractResponse {
    private UUID id;
    private String contractName;
    private String createdDate;
    private BigDecimal balance;

    public static ContractResponse fromContract(Contract contract) {
        return ContractResponse.builder()
                .id(contract.getId())
                .contractName(contract.getContractName())
                .createdDate(contract.getCreatedDate().toString())
                .balance(contract.getBalance())
                .build();
    }

}
