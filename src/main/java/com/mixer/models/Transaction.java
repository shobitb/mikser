package com.mixer.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jboss.resteasy.annotations.ResponseObject;

import java.math.BigDecimal;

@ResponseObject
public class Transaction {
    private String timestamp;
    private String toAddress;
    private String fromAddress;

    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal amount;

    public Transaction() {
        // default constructor required for jackson databinding
    }

    public Transaction(final String timestamp, final String toAddress, final String fromAddress, final BigDecimal amount) {
        this.timestamp = timestamp;
        this.toAddress = toAddress;
        this.fromAddress = fromAddress;
        this.amount = amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
