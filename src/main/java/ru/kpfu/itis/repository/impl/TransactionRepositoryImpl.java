package ru.kpfu.itis.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.kpfu.itis.model.Transaction;
import ru.kpfu.itis.repository.TransactionRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Transaction> rowMapper = new TransactionRowMapper();

    private static final String SQL_INSERT =
            "INSERT INTO transactions (id, source_contract_id, target_contract_id, amount, description, created_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_BY_ID =
            "SELECT * FROM transactions WHERE id = ?";

    private static final String SQL_SELECT_BY_CONTRACT_ID =
            "SELECT * FROM transactions WHERE source_contract_id = ? OR target_contract_id = ? ORDER BY created_at DESC";

    private static final String SQL_SELECT_ALL =
            "SELECT * FROM transactions ORDER BY created_at DESC";

    private static final String SQL_SELECT_CONTRACT_ID_BY_NAME =
            "SELECT id FROM contracts WHERE contract_name = ?";

    @Override
    public Transaction save(Transaction transaction) {
        jdbcTemplate.update(SQL_INSERT,
                transaction.getId().toString(),
                transaction.getSourceContractId().toString(),
                transaction.getTargetContractId().toString(),
                transaction.getAmount(),
                transaction.getDescription(),
                Timestamp.from(transaction.getCreatedAt()));
        return transaction;
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, rowMapper, id.toString())
            );
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Transaction> findByContractId(UUID contractId) {
        String contractIdStr = contractId.toString();
        return jdbcTemplate.query(SQL_SELECT_BY_CONTRACT_ID, rowMapper, contractIdStr, contractIdStr);
    }

    @Override
    public List<Transaction> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, rowMapper);
    }

    @Override
    public List<Transaction> getTransactionsByContractName(String contractName) {
        try {
            String contractId = jdbcTemplate.queryForObject(
                    SQL_SELECT_CONTRACT_ID_BY_NAME,
                    String.class,
                    contractName
            );
            return findByContractId(UUID.fromString(contractId));
        } catch (Exception e) {
            return List.of();
        }
    }

    private static class TransactionRowMapper implements RowMapper<Transaction> {
        @Override
        public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Transaction.builder()
                    .id(UUID.fromString(rs.getString("id")))
                    .sourceContractId(UUID.fromString(rs.getString("source_contract_id")))
                    .targetContractId(UUID.fromString(rs.getString("target_contract_id")))
                    .amount(rs.getBigDecimal("amount"))
                    .description(rs.getString("description"))  // ← добавил маппинг
                    .createdAt(rs.getTimestamp("created_at").toInstant())
                    .build();
        }
    }
}
