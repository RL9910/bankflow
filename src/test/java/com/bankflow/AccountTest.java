package com.bankflow;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.bankflow.account.Account;
import com.bankflow.account.InvalidAmountException;
import com.bankflow.account.InsufficientFundsException;
import com.bankflow.user.User;

public class AccountTest {

    @Test
    void depositShouldIncreaseBalance() {

        User aliceUser = new User(
            "alice@example.com",
            "hashed-password"
        );

        Account account = new Account("Alice", aliceUser);

        account.deposit(new BigDecimal("100"));

        assertEquals(
            new BigDecimal("100"),
            account.getBalance()
        );

    }

    @Test
    void withdrawShouldDecreaseBalance(){

        User aliceUser = new User(
            "alice@example.com",
            "hashed-password"
        );

        Account account = new Account("Alice", aliceUser);

        account.deposit(new BigDecimal("500"));

        account.withdraw(new BigDecimal("200"));

        assertEquals(new BigDecimal("300"), account.getBalance());

    }

    @Test
    void depositShouldRejectNegativeAmount() {

        User aliceUser = new User(
            "alice@example.com",
            "hashed-password"
        );

        Account account = new Account("Alice", aliceUser);

        assertThrows(
            InvalidAmountException.class,
            () -> account.deposit(new BigDecimal("-500"))
        );

    }

    @Test
    void depositShouldRejectZeroAmount() {
        User aliceUser = new User(
            "alice@example.com",
            "hashed-password"
        );

        Account account = new Account("Alice", aliceUser);

        assertThrows(
            InvalidAmountException.class,
            () -> account.deposit(new BigDecimal("0"))
        );

    }

    @Test
    void withdrawShouldRejectNegativeAmount() {
        User aliceUser = new User(
            "alice@example.com",
            "hashed-password"
        );

        Account account = new Account("Alice", aliceUser);

        assertThrows(
            InvalidAmountException.class,
            () -> account.withdraw(new BigDecimal("-500"))
        );
    }

    @Test
    void withdrawShouldRejectZeroAmount() {
        User aliceUser = new User(
            "alice@example.com",
            "hashed-password"
        );

        Account account = new Account("Alice", aliceUser);

        assertThrows(
            InvalidAmountException.class,
            () -> account.withdraw(new BigDecimal("0"))
        );
    }

    @Test
    void withdrawShouldRejectInsufficientFunds() {
        User aliceUser = new User(
            "alice@example.com",
            "hashed-password"
        );

        Account account = new Account("Alice", aliceUser);

        account.deposit(new BigDecimal("500"));

        assertThrows(
            InsufficientFundsException.class,
            () -> account.withdraw(new BigDecimal("1000"))
        );
    }

}