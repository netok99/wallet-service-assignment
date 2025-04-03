package com;

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
//    private String jdbcUrl;
//    private String username;
//    private String password;
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getUrl() {
//        return jdbcUrl;
//    }
//
//    public void setUrl(String jdbcUrl) {
//        this.jdbcUrl = jdbcUrl;
//    }

    @Value("${spring.datasource.schema:classpath:schema.sql}")
    private String schemaLocation;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl(jdbcUrl);
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
//        config.setUsername(username);
        config.setUsername("postgres");
//        config.setPassword(password);
        config.setPassword("postgres");
        config.setConnectionTimeout(20000);
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(10);
        config.setIdleTimeout(300000);
        config.setPoolName("springHikariCP");
        HikariDataSource dataSource = new HikariDataSource(config);
        initializeSchema(dataSource);
        return dataSource;
    }

    private void initializeSchema(HikariDataSource dataSource) {
        try {
            ClassPathResource resource = new ClassPathResource(schemaLocation.replace("classpath:", ""));
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
