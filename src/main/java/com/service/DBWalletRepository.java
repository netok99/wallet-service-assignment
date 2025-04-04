package com.service;

import com.domain.entity.Transaction;
import com.domain.entity.Wallet;
import com.domain.repository.WalletRepository;
import com.exception.InsufficientFundsException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class DBWalletRepository implements WalletRepository {

    private final JdbcTemplate jdbcTemplate;

    public DBWalletRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Wallet recoverWalletFromUser(Long userId) {
        return jdbcTemplate
            .queryForObject(
                "SELECT id, account_id FROM wallet WHERE account_id = ?",
                walletRowMapper,
                userId
            );
    }

    @Override
    public List<Transaction> retrieveTransactions() {
        return jdbcTemplate
            .query("SELECT id, wallet_id, amount, created_at FROM transaction", transactionRowMapper);
    }

    @Override
    public List<Transaction> retrieveTransactions(Long userId) {
        return jdbcTemplate
            .query(
                "SELECT id, wallet_id, amount, created_at FROM transaction WHERE wallet_id = ?",
                transactionRowMapper,
                recoverWalletFromUser(userId).id()
            );
    }

    @Override
    public List<Transaction> retrieveHistoricalBalance(Long userId, LocalDate initDate, LocalDate endDate) {
        return jdbcTemplate
            .query(
                "SELECT id, wallet_id, amount, created_at FROM transaction WHERE created_at::date " +
                    "BETWEEN ? AND ? AND wallet_id = ?",
                transactionRowMapper,
                java.sql.Date.valueOf(initDate),
                java.sql.Date.valueOf(endDate),
                recoverWalletFromUser(userId).id()
            );
    }

    @Override
    public void createTransaction(Long userId, BigDecimal amount) {
        jdbcTemplate
            .update(
                "INSERT INTO transaction (wallet_id, amount) values (?, ?)",
                recoverWalletFromUser(userId).id(),
                amount
            );
    }

    @Override
    @Transactional
    public void transferFunds(
        Long startUserId,
        Long destinationUserId,
        BigDecimal amount
    ) throws InsufficientFundsException {
        BigDecimal foundsFromStartUser = retrieveTransactions(startUserId)
            .stream()
            .map(Transaction::amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (amount.compareTo(foundsFromStartUser) > 0) {
            throw new InsufficientFundsException();
        }
        createTransaction(startUserId, amount.multiply(new BigDecimal(-1)));
        createTransaction(destinationUserId, amount);
    }

    @Override
    public List<Wallet> retrieveWallets() {
        return jdbcTemplate.query("SELECT id, account_id FROM wallet", walletRowMapper);
    }

    private final RowMapper<Wallet> walletRowMapper = (resultSet, rowNum) -> new Wallet(
        resultSet.getLong("id"),
        resultSet.getLong("account_id")
    );

    private final RowMapper<Transaction> transactionRowMapper = (resultSet, rowNum) -> new Transaction(
        resultSet.getLong("id"),
        resultSet.getLong("wallet_id"),
        resultSet.getBigDecimal("amount"),
        LocalDateTime.parse(
            resultSet.getString("created_at"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
        )
    );
}
