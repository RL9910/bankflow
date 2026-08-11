package com.bankflow;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

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

}