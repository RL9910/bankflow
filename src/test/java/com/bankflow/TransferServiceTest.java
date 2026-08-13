package com.bankflow;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransferServiceTest {

    // @Test
    // void transferShouldMoveMoneyBetweenAccounts() {

    //     Account alice = new Account(1L, "Alice");
    //     Account bob = new Account(2L, "Bob");

    //     alice.deposit(new BigDecimal("100"));

    //     TransferService transferService = new TransferService();

    //     transferService.transfer(alice, bob, new BigDecimal("50"));

    //     assertEquals(new BigDecimal("50"), alice.getBalance());
    //     assertEquals(new BigDecimal("50"), bob.getBalance());

    // }

    // @Test
    // void transferShouldRejectInsufficientFunds() {

    //     Account alice = new Account(1L, "Alice");
    //     Account bob = new Account(2L, "Bob");

    //     alice.deposit(new BigDecimal("10"));

    //     TransferService transferService = new TransferService();

    //     assertThrows(
    //         InsufficientFundsException.class,
    //         () -> transferService.transfer(alice, bob, new BigDecimal("50"))
    //     );

    //     assertEquals(new BigDecimal("10"), alice.getBalance());
    //     assertEquals(new BigDecimal("0"), bob.getBalance());

    // }

    @Test
    void transferShouldMoveMoneyBetweenAccounts() {

        AccountService accountService = new AccountService();
        TransferService transferService = new TransferService(accountService);

        Account alice = accountService.createAccount("Alice");
        Account bob = accountService.createAccount("Bob");

        alice.deposit(new BigDecimal("100"));

        transferService.transfer(
            alice.getId(),
            bob.getId(),
            new BigDecimal("50")
        );

        assertEquals(new BigDecimal("50"), alice.getBalance());
        assertEquals(new BigDecimal("50"), bob.getBalance());
    }

    @Test
    void transferShouldRejectInsufficientFunds() {

        AccountService accountService = new AccountService();
        TransferService transferService = new TransferService(accountService);

        Account alice = accountService.createAccount("Alice");
        Account bob = accountService.createAccount("Bob");

        alice.deposit(new BigDecimal("10"));

        assertThrows(
            InsufficientFundsException.class,
            () -> transferService.transfer(
                alice.getId(),
                bob.getId(),
                new BigDecimal("50")
            )
        );

        assertEquals(new BigDecimal("10"), alice.getBalance());
        assertEquals(BigDecimal.ZERO, bob.getBalance());
    }

}