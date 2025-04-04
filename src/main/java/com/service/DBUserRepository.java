package com.service;

import com.domain.entity.User;
import com.domain.repository.UserRepository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DBUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public DBUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void create(User user) {
        String querySql = "INSERT INTO account (name, email) VALUES (?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(querySql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.name());
            preparedStatement.setString(2, user.email());
            return preparedStatement;
        }, keyHolder);
        String userId = Objects.requireNonNull(keyHolder.getKeys()).get("id").toString();
        jdbcTemplate.update("INSERT INTO wallet (account_id) VALUES (?);", Long.parseLong(userId));
    }

    @Override
    public List<User> recover() {
        return jdbcTemplate.query("SELECT * FROM account;", userRowMapper);
    }

    private final RowMapper<User> userRowMapper = (resultSet, rowNum) -> new User(
        resultSet.getLong("id"),
        resultSet.getString("name"),
        resultSet.getString("email")
    );
}
