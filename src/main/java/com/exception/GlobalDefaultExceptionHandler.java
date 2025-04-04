package com.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    public ResponseEntity<Void> emptyResultDataAccessExceptionHandler(
        HttpServletRequest req,
        Exception exception
    ) throws Exception {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(value = InsufficientFundsException.class)
    public ResponseEntity<Void> InsufficientFundsExceptionHandler(
        HttpServletRequest req,
        Exception exception
    ) throws Exception {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
