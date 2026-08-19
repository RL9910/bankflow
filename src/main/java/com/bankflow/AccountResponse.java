package com.bankflow;

import java.math.BigDecimal;

public class AccountResponse {

    private Long id;
    private String ownerName;
    private BigDecimal balance;

    public AccountResponse(
            Long id,
            String ownerName,
            BigDecimal balance) {

        this.id = id;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}