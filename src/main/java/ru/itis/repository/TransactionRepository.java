package ru.itis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itis.model.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query("""
    SELECT t FROM Transaction t, Contract c
    WHERE (t.sourceContractId = c.id OR t.targetContractId = c.id)
      AND c.contractName = ?1
    """)
    List<Transaction> findAllByContractName(String contractName);
}
