package com.bankflow;

import java.math.BigDecimal;

public class TransferCompletedEvent {

    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;

    public TransferCompletedEvent() {
    }

    public TransferCompletedEvent(
            Long fromAccountId,
            Long toAccountId,
            BigDecimal amount) {

        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

}