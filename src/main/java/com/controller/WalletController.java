package com.controller;

import com.controller.model.TransactionModel;
import com.controller.model.TransferModel;
import com.domain.entity.Transaction;
import com.domain.entity.Wallet;
import com.domain.repository.WalletRepository;
import com.domain.usecase.WalletUseCase;
import com.exception.InsufficientFundsException;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<BigDecimal> retrieveBalance(@RequestParam("user_id") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(walletUseCase.retrieveBalance(userId));
    }

    //
//    @GetMapping("/{walletId}/history")
//    public ResponseEntity<Void> retrieveHistoricalBalance(@PathVariable("walletId") Integer walletId) {
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }
//
    @PostMapping("/transaction/")
    public ResponseEntity<Void> createTransaction(
        @RequestBody TransactionModel transactionModel
    ) throws InsufficientFundsException {
        walletUseCase.createTransaction(transactionModel.userId(), transactionModel.amount());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/transfer/")
    public ResponseEntity<Void> transferFunds(
        @RequestBody TransferModel transferModel
    ) throws InsufficientFundsException {
        walletUseCase.transferFounds(
            transferModel.startUserId(),
            transferModel.destinationUserId(),
            transferModel.amount()
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
