package com.bankflow.transfer;

import java.math.BigDecimal;

public class TransferCompletedEvent {

    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    
    // To prevent duplication of processing by kafka
    private String eventId;

    public TransferCompletedEvent() {
    }

    public TransferCompletedEvent(
            String eventId,
            Long fromAccountId,
            Long toAccountId,
            BigDecimal amount) {

        this.eventId = eventId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }


    public String getEventId() {
        return eventId;
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