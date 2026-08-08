package com.bankflow;

import java.math.BigDecimal;

public class Main{

    public static void main(String[] args){

        Account alice = new Account("Alice");
        Account bob = new Account("Bob");

        alice.deposit(new BigDecimal("1000"));
        bob.deposit(new BigDecimal("500"));
    
        alice.deposit(new BigDecimal("200"));
        bob.withdraw(new BigDecimal("100"));

        alice.deposit(new BigDecimal("-500"));
        bob.withdraw(new BigDecimal("1000"));

        System.out.println("Alice balance: " + alice.getBalance());
        System.out.println("Bob balance: " + bob.getBalance());

    }


}