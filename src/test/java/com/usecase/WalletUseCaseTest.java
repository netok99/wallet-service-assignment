package com.usecase;

import com.domain.entity.Transaction;
import com.domain.entity.Wallet;
import com.domain.repository.WalletRepository;
import com.domain.usecase.WalletUseCase;
import com.exception.InsufficientFundsException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WalletUseCaseTest {

    private final WalletRepository mockWalletRepository = mock(WalletRepository.class);
    private final WalletUseCase walletUseCase = new WalletUseCase(mockWalletRepository);

    @Test
    public void retrieveWallets() {
        List<Wallet> wallets = List.of(
            new Wallet(1L, 1L),
            new Wallet(2L, 2L)
        );
        when(mockWalletRepository.retrieveWallets()).thenReturn(wallets);
        List<Wallet> actual = walletUseCase.retrieveWallets();

        verify(mockWalletRepository, times(1)).retrieveWallets();
        Assertions.assertEquals(wallets, actual);
    }

    @Test
    public void retrieveBalanceFromUser() {
        Long userId = 1L;
        Long walletId = 1L;
        List<Transaction> transactions = List.of(
            new Transaction(
                1L,
                walletId,
                new BigDecimal(10),
                LocalDate.of(2025, Month.APRIL, 2).atStartOfDay()
            ),
            new Transaction(
                2L,
                walletId,
                new BigDecimal(30),
                LocalDate.of(2025, Month.APRIL, 3).atStartOfDay()
            )
        );
        when(mockWalletRepository.retrieveTransactions(userId)).thenReturn(transactions);
        BigDecimal expected = new BigDecimal(40);
        BigDecimal actual = walletUseCase.retrieveBalance(userId);

        verify(mockWalletRepository, times(1)).retrieveTransactions(userId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void createTransaction() throws InsufficientFundsException {
        Long userId = 1L;
        Long walletId = 1L;
        List<Transaction> transactions = List.of(
            new Transaction(
                1L,
                walletId,
                new BigDecimal(10),
                LocalDate.of(2025, Month.APRIL, 2).atStartOfDay()
            ),
            new Transaction(
                2L,
                walletId,
                new BigDecimal(30),
                LocalDate.of(2025, Month.APRIL, 3).atStartOfDay()
            )
        );

        BigDecimal transactionAmount = new BigDecimal(20);

        when(mockWalletRepository.retrieveTransactions(userId)).thenReturn(transactions);
        BigDecimal beforeBalance = walletUseCase.retrieveBalance(userId);

        walletUseCase.createTransaction(userId, transactionAmount);

        List<Transaction> newTransactions = new ArrayList<>(transactions);
        newTransactions.add(
            new Transaction(
                3L,
                walletId,
                transactionAmount,
                LocalDate.of(2025, Month.APRIL, 3).atStartOfDay()
            )
        );
        when(mockWalletRepository.retrieveTransactions(userId)).thenReturn(newTransactions);
        BigDecimal actualBalance = walletUseCase.retrieveBalance(userId);

        verify(mockWalletRepository, times(1)).createTransaction(userId, transactionAmount);
        Assertions.assertEquals(beforeBalance.add(transactionAmount), actualBalance);
    }

    @Test
    public void retrieveHistoricalBalance() {
        Long userId = 1L;
        Long walletId = 1L;
        List<Transaction> transactions = List.of(
            new Transaction(
                1L,
                walletId,
                new BigDecimal(10),
                LocalDate.of(2025, Month.APRIL, 2).atStartOfDay()
            ),
            new Transaction(
                2L,
                walletId,
                new BigDecimal(30),
                LocalDate.of(2025, Month.APRIL, 3).atStartOfDay()
            )
        );

        LocalDate initDate = LocalDate.of(2025, Month.APRIL, 2);
        LocalDate endDate = LocalDate.of(2025, Month.APRIL, 3);

        when(
            mockWalletRepository.retrieveHistoricalBalance(userId, initDate, endDate)
        ).thenReturn(transactions);

        BigDecimal expected = new BigDecimal(40);
        BigDecimal actual = walletUseCase.retrieveHistoricalBalance(userId, initDate, endDate);

        verify(mockWalletRepository, times(1))
            .retrieveHistoricalBalance(userId, initDate, endDate);
        Assertions.assertEquals(expected, actual);
    }
}
