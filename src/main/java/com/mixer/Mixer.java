package com.mixer;

import com.mixer.models.Destination;
import com.mixer.service.JobCoinService;
import com.mixer.models.UserOptions;
import org.slf4j.Logger;
import org.slf4j.simple.SimpleLoggerFactory;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;

import static com.mixer.Constants.HOUSE_ADDRESS;
import static com.mixer.Constants.MIXER_FEE_ADDRESS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * This is the core of the mixer. It transfers the deposited amount into the destination addresses
 * based on the user-specified payment distribution and delays, and after taking a fee cut (also
 * defined by the user)
 */
public class Mixer {
    private static final Logger LOG = new SimpleLoggerFactory().getLogger(
            Mixer.class.getSimpleName());

    private final String depositAddress;
    private final PayoutHandler payoutHandler;
    private final JobCoinService service;
    private final ScheduledExecutorService scheduler;

    public Mixer(final JobCoinService service, final PayoutHandler payoutHandler,
                 final ScheduledExecutorService scheduler) {
        this.depositAddress = UUID.randomUUID().toString().replace("-", "");
        this.payoutHandler = payoutHandler;
        this.service = service;
        this.scheduler = scheduler;
    }

    public String getDepositAddress() {
        return depositAddress;
    }

    /**
     * Posts transactions from mixer-specified deposit address to house address and pays out to the destination addresses
     * according to user-specified configuration (i.e., specified addresses, honoring the distribution and payout delay).
     * <p>
     * Also posts a fee transaction based on the fee specified by the user.
     * <p>
     * If there is no balance in the deposit address, this method returns (no-op) without any transactions
     * <p>
     *
     * @param userOptions
     */
    public void mix(final UserOptions userOptions) {
        try {
            final BigDecimal balance = service.getBalance(depositAddress);
            if (balance.compareTo(BigDecimal.ZERO) == 0) {
                // no op
                return;
            }

            // post a fee txn and house txn. clearly, this is a distributed transaction and introduces
            // interesting failure scenarios, and will likely need something more complex in production code
            // such as idempotence ids, or two phase commits, or an API that accepts multiple destinations, etc.
            // but for this programming exercise, i'm keeping it simple.
            final BigDecimal fee = balance.multiply(userOptions.getFeePercent());
            if (fee.compareTo(BigDecimal.ZERO) != 0) {
                service.postTransaction(depositAddress, MIXER_FEE_ADDRESS, fee);
            }

            final BigDecimal balanceMinusFee = balance.subtract(fee);
            service.postTransaction(depositAddress, HOUSE_ADDRESS, balanceMinusFee);

            LOG.info(String.format("Moved %s jobcoins from depositAddress = %s to house, with %s jobcoins in fees",
                    balanceMinusFee, depositAddress, fee));

            // schedule payout transactions
            for (final Destination destination : userOptions.getDestinations()) {
                final Runnable payoutRunnable = payoutHandler.createPayoutRunnable(
                        destination.getAddress(), balanceMinusFee.multiply(destination.getDistribution()));
                scheduler.schedule(payoutRunnable, destination.getPayoutDelaySeconds(), SECONDS);
                LOG.info(String.format("Scheduled payout for %s with %s sec delay",
                        destination.getAddress(), destination.getPayoutDelaySeconds()));
            }

        } catch (final Exception ex) {
            LOG.error("Exception in deposit clearance", ex);
        }
    }
}