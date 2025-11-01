package ru.kpfu.itis.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.kpfu.itis.model.Contract;
import ru.kpfu.itis.repository.ContractRepository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class ContractRepositoryImpl implements ContractRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ContractRowMapper rowMapper = new ContractRowMapper();
    private final String SQL_SELECT_BY_CONTRACT_NAME = "SELECT * FROM contracts WHERE contract_name = ?";
    private final String SQL_INSERT =  "INSERT INTO contracts(\"contract_name\", \"created_date\", \"balance\") VALUES (?, ? , ?)";

    @Override
    public Optional<Contract> findByContractName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_SELECT_BY_CONTRACT_NAME, rowMapper, name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Contract save(Contract contract) {
        if (contract.getId() == null) {
            contract.setId(UUID.randomUUID());
        }

        jdbcTemplate.update(SQL_INSERT,
                contract.getId().toString(),
                contract.getContractName(),
                Timestamp.from(contract.getCreatedDate()),
                contract.getBalance());

        return contract;
    }

    @Override
    public Contract update(Contract contract) {
        return null;
    }

    @Override
    public BigDecimal getBalanceByContractName(String name) {
        return Optional.ofNullable(findByContractName(name).get().getBalance()).orElse(BigDecimal.ZERO);
    }

    private static final class ContractRowMapper implements RowMapper<Contract> {

        @Override
        public Contract mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Contract.builder()
                    .id(UUID.fromString(rs.getString("id")))
                    .contractName(rs.getString("contract_name"))
                    .createdDate(rs.getTimestamp("created_date").toInstant())
                    .balance(rs.getBigDecimal("balance"))
                    .build();
        }
    }
}
