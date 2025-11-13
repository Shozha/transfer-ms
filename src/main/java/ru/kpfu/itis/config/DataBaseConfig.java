package ru.kpfu.itis.config;

import lombok.experimental.UtilityClass;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@UtilityClass
public class DataBaseConfig {

    public final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());

    private DataSource dataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/transfer-db");
        dataSource.setUser("postgres");
        dataSource.setPassword("qwerty007");
        return dataSource;
    }

}
