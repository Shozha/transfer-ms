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
    private final TransactionRowMapper rowMapper = new TransactionRowMapper();

    private final String SQL_INSERT =
            "INSERT INTO transactions(id, source_contract_id, target_contract_id, amount, description, created_at) VALUES (?, ?, ?, ?, ?, ?)";
    private final String SQL_SELECT_BY_ID =
            "SELECT * FROM transactions WHERE id = ?";
    private final String SQL_SELECT_BY_CONTRACT_ID =
            "SELECT * FROM transactions WHERE source_contract_id = ? OR target_contract_id = ?";
    private final String SQL_SELECT_BY_CONTRACT_NAME =
            "SELECT t.* FROM transactions t JOIN contracts c ON t.source_contract_id = c.id OR t.target_contract_id = c.id WHERE c.contract_name = ?";
    private final String SQL_SELECT_ALL =
            "SELECT * FROM transactions";

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
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, rowMapper, id.toString()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Transaction> findByContractId(UUID contractId) {
        return jdbcTemplate.query(SQL_SELECT_BY_CONTRACT_ID, rowMapper, contractId.toString(), contractId.toString());
    }

    @Override
    public List<Transaction> findByContractName(String contractName) {
        return jdbcTemplate.query(SQL_SELECT_BY_CONTRACT_NAME, rowMapper, contractName);
    }

    @Override
    public List<Transaction> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, rowMapper);
    }

    private static final class TransactionRowMapper implements RowMapper<Transaction> {
        @Override
        public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Transaction.builder()
                    .id(UUID.fromString(rs.getString("id")))
                    .sourceContractId(UUID.fromString(rs.getString("source_contract_id")))
                    .targetContractId(UUID.fromString(rs.getString("target_contract_id")))
                    .amount(rs.getBigDecimal("amount"))
                    .description(rs.getString("description"))
                    .createdAt(rs.getTimestamp("created_at").toInstant())
                    .build();
        }
    }
}