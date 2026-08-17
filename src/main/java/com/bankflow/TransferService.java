package com.bankflow;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRecordRepository transactionRecordRepository;

    public TransferService(
            AccountRepository accountRepository,
            TransactionRecordRepository transactionRecordRepository) {

        this.accountRepository = accountRepository;
        this.transactionRecordRepository = transactionRecordRepository;
    }

    // public void transfer(Account from, Account to, BigDecimal amount) {

    //     from.withdraw(amount);

    //     to.deposit(amount);

    // }

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

    }

}