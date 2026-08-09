package com.bankflow;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountTest {

    @Test
    void depositShouldIncreaseBalance() {

        Account account = new Account("Alice");

        account.deposit(new BigDecimal("100"));

        assertEquals(
            new BigDecimal("100"),
            account.getBalance()
        );

    }

    @Test
    void withdrawShouldDecreaseBalance(){

        Account account = new Account("Alice");

        account.deposit(new BigDecimal("500"));

        account.withdraw(new BigDecimal("200"));

        assertEquals(new BigDecimal("300"), account.getBalance());

    }

    @Test
    void depositShouldRejectNegativeAmount() {
        
        Account account = new Account("Alice");

        assertThrows(
            InvalidAmountException.class,
            () -> account.deposit(new BigDecimal("-500"))
        );

    }

    @Test
    void depositShouldRejectZeroAmount() {

        Account account = new Account("Alice");

        assertThrows(
            InvalidAmountException.class,
            () -> account.deposit(new BigDecimal("0"))
        );

    }

    @Test
    void withdrawShouldRejectNegativeAmount() {

        Account account = new Account("Alice");

        assertThrows(
            InvalidAmountException.class,
            () -> account.withdraw(new BigDecimal("-500"))
        );
    }

    @Test
    void withdrawShouldRejectZeroAmount() {

        Account account = new Account("Alice");

        assertThrows(
            InvalidAmountException.class,
            () -> account.withdraw(new BigDecimal("0"))
        );
    }

    @Test
    void withdrawShouldRejectInsufficientFunds() {

        Account account = new Account("Alice");

        account.deposit(new BigDecimal("500"));

        assertThrows(
            InsufficientFundsException.class,
            () -> account.withdraw(new BigDecimal("1000"))
        );
    }

}