package com.mixer.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;

public class PostTransaction {
    private String fromAddress;
    private String toAddress;
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal amount;

    public PostTransaction() {
        // default constructor required for jackson databinding
    }

    public PostTransaction(String fromAddress, String toAddress, BigDecimal amount) {
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.amount = amount;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
