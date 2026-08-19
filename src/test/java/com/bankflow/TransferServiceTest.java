package com.bankflow;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.springframework.cache.CacheManager;

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

        User aliceUser = mock(User.class);
        User bobUser = mock(User.class);

        when(aliceUser.getId()).thenReturn(1L);
        when(bobUser.getId()).thenReturn(2L);

        Account alice = new Account("Alice", aliceUser);
        Account bob = new Account("Bob", bobUser);

        alice.deposit(new BigDecimal("100"));

        when(accountRepository.findByIdAndUserId(1L, 1L))
            .thenReturn(Optional.of(alice));

        when(accountRepository.findById(2L))
            .thenReturn(Optional.of(bob));

        TransactionRecordRepository transactionRecordRepository =
            mock(TransactionRecordRepository.class);

        CacheManager cacheManager = mock(CacheManager.class);

        TransferService transferService =
            new TransferService(
                accountRepository,
                transactionRecordRepository,
                cacheManager
            );

        transferService.transfer(
            1L,
            2L,
            new BigDecimal("50"),
            aliceUser
        );

        assertEquals(new BigDecimal("50"), alice.getBalance());
        assertEquals(new BigDecimal("50"), bob.getBalance());
    }

    @Test
    void transferShouldRejectInsufficientFunds() {

        AccountRepository accountRepository = mock(AccountRepository.class);

        User aliceUser = mock(User.class);
        User bobUser = mock(User.class);

        when(aliceUser.getId()).thenReturn(1L);
        when(bobUser.getId()).thenReturn(2L);

        Account alice = new Account("Alice", aliceUser);
        Account bob = new Account("Bob", bobUser);

        alice.deposit(new BigDecimal("10"));

        when(accountRepository.findByIdAndUserId(1L, 1L))
            .thenReturn(Optional.of(alice));

        when(accountRepository.findById(2L))
            .thenReturn(Optional.of(bob));

        TransactionRecordRepository transactionRecordRepository =
            mock(TransactionRecordRepository.class);

        CacheManager cacheManager = mock(CacheManager.class);

        TransferService transferService =
            new TransferService(
                accountRepository,
                transactionRecordRepository,
                cacheManager
            );

        assertThrows(
            InsufficientFundsException.class,
            () -> transferService.transfer(
                1L,
                2L,
                new BigDecimal("50"),
                aliceUser
            )
        );

        assertEquals(new BigDecimal("10"), alice.getBalance());
        assertEquals(BigDecimal.ZERO, bob.getBalance());
    }


    @Test
    void transferShouldRejectSourceAccountOwnedByAnotherUser() {

        AccountRepository accountRepository = mock(AccountRepository.class);

        User aliceUser = mock(User.class);
        when(aliceUser.getId()).thenReturn(1L);

        User bobUser = mock(User.class);
        when(bobUser.getId()).thenReturn(2L);

        Account bobAccount = new Account("Bob", bobUser);
        bobAccount.deposit(new BigDecimal("100"));

        when(accountRepository.findByIdAndUserId(2L, 1L))
            .thenReturn(Optional.empty());

        TransactionRecordRepository transactionRecordRepository =
            mock(TransactionRecordRepository.class);

        CacheManager cacheManager = mock(CacheManager.class);

        TransferService transferService =
            new TransferService(
                accountRepository,
                transactionRecordRepository,
                cacheManager
            );

        assertThrows(
            AccountNotFoundException.class,
            () -> transferService.transfer(
                2L,   // Bob's account
                1L,
                new BigDecimal("50"),
                aliceUser
            )
        );
    }

}