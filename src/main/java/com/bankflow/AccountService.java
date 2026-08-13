package com.bankflow;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final Map<Long, Account> accounts = new HashMap<>();
    private long nextId = 1;

    public Account createAccount(String ownerName) {
        Account account = new Account(nextId, ownerName);
        accounts.put(nextId, account);
        nextId += 1;
        return account;
    }

    public Account getAccount(Long id) {

        Account account = accounts.get(id);
        if (account == null) {
            throw new AccountNotFoundException("Account not found");
        }
        return account;
    }

    public Account deposit(Long id, BigDecimal amount) {

        Account account = this.getAccount(id);

        account.deposit(amount);

        return account;

    }

    public Account withdraw(Long id, BigDecimal amount) {

        Account account = this.getAccount(id);

        account.withdraw(amount);

        return account;
    }

}