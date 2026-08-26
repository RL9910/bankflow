package com.bankflow.transfer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRecordRepository
        extends JpaRepository<TransactionRecord, Long> {

        List<TransactionRecord> findByFromAccountIdOrToAccountId(
            Long fromAccountId,
            Long toAccountId
        );
}