package com.mixer.models;

import java.math.BigDecimal;
import java.util.List;

/**
 * Stores user options input to the Mixer. Fee percent is a decimal value e.g. 0.01 represents 1% fee on the deposit.
 */
public class UserOptions {
    private final List<Destination> destinations;
    private final BigDecimal feePercent;

    public UserOptions(final List<Destination> destinations, final BigDecimal feePercent) {
        this.destinations = destinations;
        this.feePercent = feePercent;
    }

    public List<Destination> getDestinations() {
        return destinations;
    }

    public BigDecimal getFeePercent() {
        return feePercent;
    }
}
