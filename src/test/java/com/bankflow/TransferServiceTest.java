package com.bankflow;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

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

        AccountRepository accountRepository = mock(AccountRepository.class);

        Account alice = new Account("Alice");
        Account bob = new Account("Bob");

        alice.deposit(new BigDecimal("100"));

        when(accountRepository.findById(1L))
            .thenReturn(Optional.of(alice));

        when(accountRepository.findById(2L))
            .thenReturn(Optional.of(bob));

        TransferService transferService = new TransferService(accountRepository);

        transferService.transfer(
            1L,
            2L,
            new BigDecimal("50")
        );

        assertEquals(new BigDecimal("50"), alice.getBalance());
        assertEquals(new BigDecimal("50"), bob.getBalance());
    }

    @Test
    void transferShouldRejectInsufficientFunds() {

        AccountRepository accountRepository = mock(AccountRepository.class);

        Account alice = new Account("Alice");
        Account bob = new Account("Bob");

        alice.deposit(new BigDecimal("10"));

        when(accountRepository.findById(1L))
            .thenReturn(Optional.of(alice));

        when(accountRepository.findById(2L))
            .thenReturn(Optional.of(bob));

        TransferService transferService = new TransferService(accountRepository);

        assertThrows(
            InsufficientFundsException.class,
            () -> transferService.transfer(
                1L,
                2L,
                new BigDecimal("50")
            )
        );

        assertEquals(new BigDecimal("10"), alice.getBalance());
        assertEquals(BigDecimal.ZERO, bob.getBalance());
    }
}