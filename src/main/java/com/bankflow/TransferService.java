package com.bankflow;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TransferService {

    private final AccountRepository accountRepository;

    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // public void transfer(Account from, Account to, BigDecimal amount) {

    //     from.withdraw(amount);

    //     to.deposit(amount);

    // }

    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount) {

        Account from = accountRepository.findById(fromId)
            .orElseThrow(() -> new AccountNotFoundException("Source account not found"));

        Account to = accountRepository.findById(toId)
            .orElseThrow(() -> new AccountNotFoundException("Destination account not found"));

        from.withdraw(amount);
        to.deposit(amount);

    }

}