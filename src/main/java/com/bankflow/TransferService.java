package com.bankflow;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
public class TransferService {

    private final AccountService accountService;

    public TransferService(AccountService accountService) {
        this.accountService = accountService;
    }

    // public void transfer(Account from, Account to, BigDecimal amount) {

    //     from.withdraw(amount);

    //     to.deposit(amount);

    // }

    public void transfer(Long fromId, Long toId, BigDecimal amount) {

        Account from = accountService.getAccount(fromId);
        Account to = accountService.getAccount(toId);

        from.withdraw(amount);
        to.deposit(amount);

    }

}