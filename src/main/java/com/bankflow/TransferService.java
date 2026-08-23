package com.bankflow;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

@Service
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final CacheManager cacheManager;
    private final TransferEventProducer transferEventProducer;

    public TransferService(
            AccountRepository accountRepository,
            TransactionRecordRepository transactionRecordRepository,
            CacheManager cacheManager,
            TransferEventProducer transferEventProducer) {

        this.accountRepository = accountRepository;
        this.transactionRecordRepository = transactionRecordRepository;
        this.cacheManager = cacheManager;
        this.transferEventProducer = transferEventProducer;
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
                from.getId(),
                to.getId(),
                amount
            );

        transferEventProducer.publish(event);

    }

}