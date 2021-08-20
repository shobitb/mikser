package com.mixer.models;

import java.math.BigDecimal;

/**
 * Holds a destination address and configuration like percent payment distribution and
 * final payout delay in seconds
 */
public class Destination {
    private final String address;
    private final BigDecimal distribution;
    private final int payoutDelaySeconds;

    public Destination(final String address, final BigDecimal distribution, final int payoutDelaySeconds) {
        this.address = address;
        this.distribution = distribution;
        this.payoutDelaySeconds = payoutDelaySeconds;
    }

    public String getAddress() {
        return address;
    }

    public BigDecimal getDistribution() {
        return distribution;
    }

    public int getPayoutDelaySeconds() {
        return payoutDelaySeconds;
    }
}
