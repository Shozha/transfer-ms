package ru.itis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.mapper.ContractMapper;
import ru.itis.dto.response.ContractResponse;
import ru.itis.model.Contract;
import ru.itis.service.ContractService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transfers")
public class ContractController {

    private final ContractService contractService;
    private final ContractMapper contractMapper;

    @GetMapping("/contract/{name}")
    public ResponseEntity<ContractResponse> getContract(@PathVariable("name") String name) {
        log.info("Fetching contract by name: {}", name);
        Contract contract = contractService.getByContractName(name);
        log.debug("Contract found: id={}", contract.getId());
        return ResponseEntity.ok(contractMapper.toContractResponse(contract));
    }

    @PostMapping("/contract")
    public ResponseEntity<ContractResponse> createContract() {
        log.info("Creating new empty contract");
        Contract contract = contractService.createEmptyContract();
        log.info("New contract created with id: {}, name: {}", contract.getId(), contract.getContractName());
        return ResponseEntity.status(HttpStatus.CREATED).body(contractMapper.toContractResponse(contract)) ;
    }

}
