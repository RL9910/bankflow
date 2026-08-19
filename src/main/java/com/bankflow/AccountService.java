package com.bankflow;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

@Service
public class AccountService {

    // private final Map<Long, Account> accounts = new HashMap<>();
    // private long nextId = 1;

    private final AccountRepository accountRepository;
    private final TransactionRecordRepository transactionRecordRepository;

    public AccountService(
            AccountRepository accountRepository,
            TransactionRecordRepository transactionRecordRepository) {

        this.accountRepository = accountRepository;
        this.transactionRecordRepository = transactionRecordRepository;
    }

    public Account createAccount(String ownerName, User user) {
        // Account account = new Account(nextId, ownerName);
        // accounts.put(nextId, account);
        // nextId += 1;
        // return account;
        Account account = new Account(ownerName, user);
        return accountRepository.save(account);
    }

    public Account getAccount(Long id) {

        // Account account = accounts.get(id);
        // if (account == null) {
        //     throw new AccountNotFoundException("Account not found");
        // }
        // return account;
        return accountRepository.findById(id)
        .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        
    }

    @Cacheable(
        value = "accounts",
        key = "#accountId + ':' + #user.id"
    )
    public Account getAccountForUser(Long accountId, User user) {

        System.out.println("QUERYING DATABASE");

        return accountRepository
            .findByIdAndUserId(accountId, user.getId())
            .orElseThrow(() -> 
                new AccountNotFoundException("Account not found")
            );
    }

    // public Account deposit(Long id, BigDecimal amount) {

    //     Account account = this.getAccount(id);

    //     account.deposit(amount);

    //     return accountRepository.save(account);

    // }

    // public Account withdraw(Long id, BigDecimal amount) {

    //     Account account = this.getAccount(id);

    //     account.withdraw(amount);

    //     return accountRepository.save(account);
    // }

    @Transactional
    public Account depositForUser(Long id, BigDecimal amount, User user) {

        Account account = getAccountForUser(id, user);

        account.deposit(amount);

        TransactionRecord record = new TransactionRecord(
            "DEPOSIT",
            amount,
            null,
            account.getId()
        );

        transactionRecordRepository.save(record);

        return accountRepository.save(account);
    }

    @Transactional
    public Account withdrawForUser(
            Long id,
            BigDecimal amount,
            User user) {

        Account account = getAccountForUser(id, user);

        account.withdraw(amount);

        TransactionRecord record = new TransactionRecord(
            "WITHDRAWAL",
            amount,
            account.getId(),
            null
        );

        transactionRecordRepository.save(record);

        return accountRepository.save(account);
    }

    public List<TransactionRecord> getTransactionsForUser(
            Long accountId,
            User user) {

        // First verify this account belongs to this user
        getAccountForUser(accountId, user);

        return transactionRecordRepository
            .findByFromAccountIdOrToAccountId(
                accountId,
                accountId
            );
    }

}