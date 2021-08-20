package com.mixer.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jboss.resteasy.annotations.ResponseObject;

import java.math.BigDecimal;
import java.util.List;

@ResponseObject
public class Address {
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal balance;
    private List<Transaction> transactions;

    public Address() {
        // default constructor required for jackson databinding
    }

    public Address(final BigDecimal balance, final List<Transaction> transactions) {
        this.balance = balance;
        this.transactions = transactions;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
