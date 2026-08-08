package com.bankflow;

import java.math.BigDecimal;

public class Account{

    private String ownerName;
    private BigDecimal balance; 

    public Account(String ownerName){

        this.ownerName = ownerName;
        this.balance = BigDecimal.ZERO;
    }

    public void deposit(BigDecimal amount){
        //BigDecimal is a class/object and need .add() instead of + for primative (int or double)
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount){
        if (balance.subtract(amount).compareTo(BigDecimal.ZERO) < 0){
            throw new InsufficientFundsException("Insufficient balance");
        }
        balance = balance.subtract(amount);
    }

    public String getOwnerName(){
        return ownerName;
    }

    public BigDecimal getBalance(){
        return balance;
    }

}