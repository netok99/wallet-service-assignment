package com.domain.usecase;

import com.domain.entity.Transaction;
import com.domain.entity.Wallet;
import com.domain.repository.WalletRepository;
import com.exception.InsufficientFundsException;
import java.math.BigDecimal;
import java.util.List;

public class WalletUseCase {

    private final WalletRepository walletRepository;

    public WalletUseCase(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public List<Wallet> retrieveWallets() {
        return walletRepository.retrieveWallets();
    }

    public BigDecimal retrieveBalance(Long userId) {
        return walletRepository
            .retrieveTransactions(userId)
            .stream()
            .map(Transaction::amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Transaction> retrieveTransactions() {
        return walletRepository.retrieveTransactions();
    }

    public void createTransaction(Long userId, BigDecimal amount) throws InsufficientFundsException {
        boolean operationIsLessThanZero = amount.add(retrieveBalance(userId)).compareTo(BigDecimal.ZERO) < 0;
        if (operationIsLessThanZero) {
            throw new InsufficientFundsException();
        }
        walletRepository.createTransaction(userId, amount);
    }

    public void transferFounds(
        Long startUserId,
        Long destinationUserId,
        BigDecimal amount
    ) throws InsufficientFundsException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transferência deve ser maior que zero");
        }
        walletRepository.transferFunds(startUserId, destinationUserId, amount);
    }
}
