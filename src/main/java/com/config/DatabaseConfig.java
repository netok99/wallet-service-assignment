package com.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.stream.Collectors;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DatabaseConfig {

    @Value("${spring.datasource.schema:classpath:schema.sql}")
    private String schemaLocation;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    @Value("${spring.datasource.hikari.connection-timeout}")
    private Long hikariConnectionTimeout;

    @Value("${spring.datasource.hikari.minimum-idle}")
    private int hikariMinimumIdle;

    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private int hikariMaximumPoolSize;

    @Value("${spring.datasource.hikari.idle-timeout}")
    private Long hikariIdleTimeout;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(datasourceUrl);
        config.setUsername(datasourceUsername);
        config.setPassword(datasourcePassword);
        config.setConnectionTimeout(hikariConnectionTimeout);
        config.setMinimumIdle(hikariMinimumIdle);
        config.setMaximumPoolSize(hikariMaximumPoolSize);
        config.setIdleTimeout(hikariIdleTimeout);
        config.setPoolName("springHikariCP");
        HikariDataSource dataSource = new HikariDataSource(config);
        initializeSchema(dataSource);
        return dataSource;
    }

    private void initializeSchema(HikariDataSource dataSource) {
        try {
            ClassPathResource resource = new ClassPathResource(
                schemaLocation.replace("classpath:", "")
            );
            String sqlQuery = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
            )
                .lines()
                .collect(Collectors.joining("\n"));
            try (Connection conn = dataSource.getConnection(); Statement statement = conn.createStatement()) {
                statement.execute(sqlQuery);
                System.out.println("Schema inicializado com sucesso");
            }
        } catch (Exception exception) {
            throw new RuntimeException("Erro ao inicializar schema do banco de dados", exception);
        }
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
