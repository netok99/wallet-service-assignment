package com.controller;

import com.controller.model.TransactionModel;
import com.controller.model.TransferModel;
import com.domain.entity.Transaction;
import com.domain.entity.Wallet;
import com.domain.repository.WalletRepository;
import com.domain.usecase.WalletUseCase;
import com.exception.InsufficientFundsException;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("wallet")
public class WalletController {

    private final WalletUseCase walletUseCase;
    private final String customDateTimeFormat = "dd/MM/yyyy";

    public WalletController(WalletRepository walletRepository) {
        this.walletUseCase = new WalletUseCase(walletRepository);
    }

    @GetMapping("/")
    public ResponseEntity<List<Wallet>> retrieveWallets() {
        return ResponseEntity.status(HttpStatus.OK).body(walletUseCase.retrieveWallets());
    }

    @GetMapping("/transaction/")
    public ResponseEntity<List<Transaction>> retrieveTransactions() {
        return ResponseEntity.status(HttpStatus.OK).body(walletUseCase.retrieveTransactions());
    }

    @GetMapping("/balance/")
    public ResponseEntity<BigDecimal> retrieveBalance(
        @NonNull @RequestParam("user_id") Long userId,
        @RequestParam(name = "init_date", required = false)
        @DateTimeFormat(pattern = customDateTimeFormat)
        LocalDate initDate,
        @RequestParam(name = "end_date", required = false)
        @DateTimeFormat(pattern = customDateTimeFormat)
        LocalDate endDate
    ) {
        if (initDate == null || endDate == null) {
            return ResponseEntity.status(HttpStatus.OK).body(walletUseCase.retrieveBalance(userId));
        }
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(walletUseCase.retrieveHistoricalBalance(userId, initDate, endDate));
    }

    @PostMapping("/transaction/")
    public ResponseEntity<Void> createTransaction(
        @Valid @RequestBody TransactionModel transactionModel
    ) throws InsufficientFundsException {
        walletUseCase.createTransaction(transactionModel.userId(), transactionModel.amount());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/transfer/")
    public ResponseEntity<Void> transferFunds(
        @Valid @RequestBody TransferModel transferModel
    ) throws InsufficientFundsException {
        walletUseCase.transferFounds(
            transferModel.startUserId(),
            transferModel.destinationUserId(),
            transferModel.amount()
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
