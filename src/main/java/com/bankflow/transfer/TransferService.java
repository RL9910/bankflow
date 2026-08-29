package com.bankflow.transfer;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.UUID;

import com.bankflow.outbox.OutboxEventRepository;
import com.bankflow.outbox.OutboxEvent;


import com.bankflow.user.User;
import com.bankflow.account.Account;
import com.bankflow.account.AccountNotFoundException;
import com.bankflow.account.AccountRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final CacheManager cacheManager;
    // private final TransferEventProducer transferEventProducer;
    private final ObjectMapper objectMapper;
    private final OutboxEventRepository outboxEventRepository;

    private static final Logger log =
        LoggerFactory.getLogger(TransferService.class);

    public TransferService(
            AccountRepository accountRepository,
            TransactionRecordRepository transactionRecordRepository,
            CacheManager cacheManager,
            // TransferEventProducer transferEventProducer
            OutboxEventRepository outboxEventRepository,
            ObjectMapper objectMapper) {

        this.accountRepository = accountRepository;
        this.transactionRecordRepository = transactionRecordRepository;
        this.cacheManager = cacheManager;
        // this.transferEventProducer = transferEventProducer;
        this.outboxEventRepository = outboxEventRepository;
        this.objectMapper = objectMapper;
    }

    // public void transfer(Account from, Account to, BigDecimal amount) {

    //     from.withdraw(amount);

    //     to.deposit(amount);

    // }


    // @CacheEvict(
    //     value = "accounts",
    //     allEntries = true
    // )
    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount, User currentUser) {

        log.info(
            "Transfer requested: fromAccount={}, toAccount={}, amount={}",
            fromId,
            toId,
            amount
        );

        Account from = accountRepository
            .findByIdAndUserId(fromId, currentUser.getId())
            .orElseThrow(() ->
                new AccountNotFoundException("Source account not found")
            );

        Account to = accountRepository
            .findById(toId)
            .orElseThrow(() -> 
                new AccountNotFoundException("Destination account not found")
            );

        from.withdraw(amount);
        to.deposit(amount);

        TransactionRecord record = new TransactionRecord(
            "TRANSFER",
            amount,
            from.getId(),
            to.getId()
        );

        transactionRecordRepository.save(record);

        Cache accountsCache = cacheManager.getCache("accounts");

        if (accountsCache != null) {
            accountsCache.evict(
                from.getId() + ":" + currentUser.getId()
            );

            accountsCache.evict(
                to.getId() + ":" + to.getUser().getId()
            );
        }

        TransferCompletedEvent event = 
            new TransferCompletedEvent(
                UUID.randomUUID().toString(),
                from.getId(),
                to.getId(),
                amount
            );

        try {

            String payload = objectMapper.writeValueAsString(event);

            OutboxEvent outboxEvent =
                new OutboxEvent(
                    "TRANSFER_COMPLETED",
                    payload
                );

            outboxEventRepository.save(outboxEvent);


        } catch (JsonProcessingException e) {
            throw new RuntimeException(
                "Failed to serialize transfer event",
                e
            );
        }

        // transferEventProducer.publish(event);

        log.info(
            "Transfer completed: fromAccount={}, toAccount={}, amount={}",
            fromId,
            toId,
            amount
        );

    }

}