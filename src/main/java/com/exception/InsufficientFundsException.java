package com.exception;

public class InsufficientFundsException extends Exception {

    public InsufficientFundsException() {
        super("Saldo insuficiente para realizar a transição");
    }
}
