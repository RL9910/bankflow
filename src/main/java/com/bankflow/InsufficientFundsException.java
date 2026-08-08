package com.bankflow;

public class InsufficientFundsException extends RuntimeException{

    public InsufficientFundsException(String message){
        super(message);
    }

}