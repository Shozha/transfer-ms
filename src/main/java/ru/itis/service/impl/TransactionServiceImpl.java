package ru.itis.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.client.CardsClient;
import ru.itis.client.UserRestrictionsClient;
import ru.itis.dto.response.client.CardResponse;
import ru.itis.dto.response.client.UserRestrictionResponse;
import ru.itis.exception.InvalidAmountException;
import ru.itis.exception.TransactionNotFoundException;
import ru.itis.exception.UserBlockedException;
import ru.itis.model.Contract;
import ru.itis.model.Transaction;
import ru.itis.repository.TransactionRepository;
import ru.itis.service.ContractService;
import ru.itis.service.TransactionService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ContractService contractService;
    private final UserRestrictionsClient userRestrictionsClient;
    private final CardsClient cardsClient;
    private final ExecutorService httpCallsExecutor;

    public static final String BLOCK_TYPE_TOTAL = "TOTAL";

    @Override
    @Transactional
    public Transaction save(String sourceContractName, String targetContractName, BigDecimal amount, String description) {
        log.info("Initiating transfer: amount={} from contract={} to contract={}", amount, sourceContractName, targetContractName);
        if (amount == null || amount.signum() <= 0) {
            throw new InvalidAmountException();
        }

        Contract source = contractService.getByContractName(sourceContractName) ;
        Contract target = contractService.getByContractName(targetContractName) ;
        //Отправляем запрос в User Restrictions
        CompletableFuture<UserRestrictionResponse> srcFuture = CompletableFuture
                .supplyAsync(
                        () -> cardsClient.getCardByContractName(source.getContractName()),
                        httpCallsExecutor
                )
                .thenApplyAsync(
                        card -> userRestrictionsClient.getRestrictions(UUID.fromString(card.getUserId())),
                        httpCallsExecutor
                );

        CompletableFuture<UserRestrictionResponse> trgFuture = CompletableFuture
                .supplyAsync(
                        () -> cardsClient.getCardByContractName(target.getContractName()),
                        httpCallsExecutor
                )
                .thenApplyAsync(
                        card -> userRestrictionsClient.getRestrictions(UUID.fromString(card.getUserId())),
                        httpCallsExecutor
                );

        CompletableFuture.allOf(srcFuture, trgFuture).join();

        UserRestrictionResponse src = srcFuture.join();
        UserRestrictionResponse trg = trgFuture.join();

        UUID sourceUserId = UUID.fromString(src.getUserId());
        UUID targetUserId = UUID.fromString(trg.getUserId());

        if (BLOCK_TYPE_TOTAL.equals(src.getBlockType())) {
            throw new UserBlockedException(sourceUserId);
        }

        if (BLOCK_TYPE_TOTAL.equals(trg.getBlockType())) {
            throw new UserBlockedException(targetUserId);
        }

        contractService.processTransfer(source, target, amount);

        return transactionRepository.save(
                Transaction.builder()
                        .sourceContractId(source.getId())
                        .targetContractId(target.getId())
                        .sourceUserId(sourceUserId)
                        .targetUserId(targetUserId)
                        .amount(amount)
                        .description(description)
                        .createdAt(Instant.now())
                        .build());
    }

    @Override
    public Transaction findById(UUID transactionId) {
        log.debug("Fetching transaction by id: {}", transactionId);
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
    }

    @Override
    public List<Transaction> findAllTransactionsByContractName(String contractName) {
        log.debug("Fetching all transactions for contract name: {}", contractName);
        return transactionRepository.findAllByContractName(contractName);
    }

    @Override
    public List<Transaction> findAllTransactions() {
        log.debug("Fetching all transactions");
        return transactionRepository.findAll();
    }
}
