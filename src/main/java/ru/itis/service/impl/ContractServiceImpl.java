package ru.itis.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.exception.ContractNotFoundException;
import ru.itis.exception.InsufficientFundsException;
import ru.itis.model.Contract;
import ru.itis.repository.ContractRepository;
import ru.itis.service.ContractService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;

    @Override
    public Contract getById(UUID id) {
        log.debug("Fetching contract by id: {}", id);
        return contractRepository.findById(id)
                .orElseThrow(() -> new ContractNotFoundException("Contract not found"));
    }

    @Override
    public Contract getByContractName(String name) {
        log.debug("Fetching contract by name: {}", name);
        return contractRepository.findContractByContractName(name)
                .orElseThrow(() -> new ContractNotFoundException(name));
    }

    @Override
    public BigDecimal getBalanceByContractName(String name) {
        return contractRepository.findContractByContractName(name)
                .map(Contract::getBalance)
                .orElse(BigDecimal.ZERO);
    }

    @Transactional
    @Override
    public void updateContractName(UUID id, Contract contract) {
        log.info("Updating contract name for id: {}, new name: {}", id, contract.getContractName());
        Contract existing = getById(contract.getId());
        existing.setContractName(contract.getContractName());
        log.debug("Contract name updated successfully");
    }

    @Transactional
    @Override
    public void processTransfer(Contract source, Contract target, BigDecimal amount) {
        if (source.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(source.getId());
        }

        source.setBalance(source.getBalance().subtract(amount));
        target.setBalance(target.getBalance().add(amount));
        log.info("Balances updated successfully. Source contract={}, Target contract={}, amount={}",
                source.getId(), target.getId(), amount);
    }

    @Override
    public Contract createEmptyContract() {
        log.info("Creating empty contract with random name");
        Contract contract = Contract.builder()
                .contractName(generateRandomContractName())
                .createdDate(Instant.now())
                .balance(BigDecimal.ZERO)
                .build();
        return contractRepository.save(contract);
    }

    private String generateRandomContractName() {
        String randomPart = UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 10)
                .toUpperCase();
        return String.format("CN%sRU116", randomPart);
    }
}
