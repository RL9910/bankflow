package com.bankflow;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
public class TransferIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {

        registry.add(
            "spring.datasource.url",
            postgres::getJdbcUrl
        );

        registry.add(
            "spring.datasource.username",
            postgres::getUsername
        );

        registry.add(
            "spring.datasource.password",
            postgres::getPassword
        );
    }

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransferService transferService;

    @Test
    @Transactional
    void transferShouldPersistBothBalanceChanges() {
        Account alice = accountRepository.save(new Account("Alice"));
        Account bob = accountRepository.save(new Account("Bob"));

        alice.deposit(new BigDecimal("100"));
        accountRepository.save(alice);

        transferService.transfer(
            alice.getId(),
            bob.getId(),
            new BigDecimal("40")
        );

        Account updatedAlice = accountRepository
            .findById(alice.getId())
            .orElseThrow();

        Account updatedBob = accountRepository
            .findById(bob.getId())
            .orElseThrow();

        assertEquals(
            new BigDecimal("60"),
            updatedAlice.getBalance()
        );

        assertEquals(
            new BigDecimal("40"),
            updatedBob.getBalance()
        );
    }

}