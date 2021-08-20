package com.mixer;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Runs a mixing thread periodically, and stops after 60 minutes.
 */
public class MixerScheduler {
    private final ScheduledExecutorService executor;

    public MixerScheduler(final ScheduledExecutorService executor
    ) {
        this.executor = executor;
    }

    public void scheduleMixing(final Runnable runnableMix) {
        final ScheduledFuture<?> future =
                executor.scheduleAtFixedRate(runnableMix, 0, 10, SECONDS);
        final Runnable canceller = () -> future.cancel(false);
        executor.schedule(canceller, 1, HOURS);
    }
}
