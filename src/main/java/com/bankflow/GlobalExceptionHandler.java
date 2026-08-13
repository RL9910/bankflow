package com.bankflow;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFound(AccountNotFoundException exception) {

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)  
            .body(exception.getMessage());
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<String> handleInvalidAmount(InvalidAmountException exception) {

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)  
            .body(exception.getMessage());
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFunds(InsufficientFundsException exception) {

        return ResponseEntity
            .status(HttpStatus.UNPROCESSABLE_ENTITY)  
            .body(exception.getMessage());
    }

}