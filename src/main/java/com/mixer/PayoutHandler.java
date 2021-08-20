package com.mixer;

import com.mixer.service.JobCoinService;
import com.mixer.service.JobCoinServiceException;
import org.slf4j.Logger;
import org.slf4j.simple.SimpleLoggerFactory;

import java.math.BigDecimal;

import static com.mixer.Constants.HOUSE_ADDRESS;

/**
 * Creates a runnable that performs payout processing
 */
public class PayoutHandler {
    private static final Logger LOG = new SimpleLoggerFactory().getLogger(
            PayoutHandler.class.getSimpleName());
    private final JobCoinService service;

    public PayoutHandler(final JobCoinService service) {
        this.service = service;
    }

    public Runnable createPayoutRunnable(final String destination, final BigDecimal amount) {
        return () -> {
            try {
                service.postTransaction(HOUSE_ADDRESS, destination, amount);
                LOG.info(String.format("Moved %s jobcoins from %s to %s",
                        amount.toPlainString(), HOUSE_ADDRESS, destination));
            } catch (final JobCoinServiceException ex) {
                throw new RuntimeException(String.format(
                        "Failure when paying out to %s", destination), ex);
            }
        };
    }

}
