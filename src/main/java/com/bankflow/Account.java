package com.bankflow;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Account{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ownerName;
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    protected Account() {}   

    public Account(String ownerName, User user) {
        this.ownerName = ownerName;
        this.user = user;
        this.balance = BigDecimal.ZERO;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void deposit(BigDecimal amount){

        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidAmountException("Invalid amount");
        }

        //BigDecimal is a class/object and need .add() instead of + for primative (int or double)
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount){

        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidAmountException("Invalid amount");
        }
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