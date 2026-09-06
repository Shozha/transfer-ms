package ru.itis.mapper;

import org.springframework.stereotype.Component;
import ru.itis.dto.response.ContractResponse;
import ru.itis.model.Contract;

@Component
public class ContractMapper {

    public ContractResponse toContractResponse(Contract contract) {
        return ContractResponse.builder()
                .contractName(contract.getContractName())
                .createdDate(contract.getCreatedDate().toString())
                .balance(contract.getBalance())
                .build();
    }
}
