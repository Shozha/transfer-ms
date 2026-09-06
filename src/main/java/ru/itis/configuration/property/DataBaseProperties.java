package ru.itis.configuration.property;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class DataBaseProperties {

    private final String url;
    private final String username;
    private final String password;
    private final String poolSize;
    private final String driverClassName;

    public DataBaseProperties (@Value("${db.url}") String url,
                               @Value("${db.username}") String username,
                               @Value("${db.password}") String password,
                               @Value("${db.hikari.max-pool-size}") String poolSize,
                               @Value("${db.driver.classname}") String driverClassName) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.poolSize = poolSize;
        this.driverClassName = driverClassName;
    }

}
