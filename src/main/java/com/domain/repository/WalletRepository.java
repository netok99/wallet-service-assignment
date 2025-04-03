package com.domain.repository;

import com.domain.entity.Transaction;
import com.domain.entity.Wallet;
import com.exception.InsufficientFundsException;
import java.math.BigDecimal;
import java.util.List;

public interface WalletRepository {
    List<Transaction> retrieveTransactions();

    List<Transaction> retrieveTransactions(Long userId);

    void createTransaction(Long userId, BigDecimal amount);

    void transferFunds(Long startUserId, Long destinationUserId, BigDecimal amount) throws InsufficientFundsException;

    List<Wallet> retrieveWallets();
}
