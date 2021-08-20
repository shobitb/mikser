package com.mixer;

import com.mixer.models.Destination;
import com.mixer.models.UserOptions;
import com.mixer.service.JobCoinService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * This is the user interface (command-line). Takes user input and initiates mixing
 */
public class MixerCLI {
    public static void main(String[] args) throws IOException {
        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");

        final BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));


        print("Enter the number of destination addresses you wish to use");
        final int num = Integer.parseInt(reader.readLine());

        final List<Destination> destinations = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            print("Enter destination address, percent distribution (e.g. 0.33 for 33%), and " +
                    "payout time delay (in seconds) comma separated, e.g. abc,0.33,10");
            final String[] inputArgs = reader.readLine().split(",");
            destinations.add(new Destination(inputArgs[0], new BigDecimal(inputArgs[1]), Integer.parseInt(inputArgs[2])));
        }

        print("Enter fee as percent of total amount (min=0.0 and max=0.03, i.e, 3%)");
        final double feePercent = Double.parseDouble(reader.readLine());
        final UserOptions userOptions = new UserOptions(destinations, BigDecimal.valueOf(feePercent));

        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(8);
        final JobCoinService service = new JobCoinService();
        final PayoutHandler payoutHandler = new PayoutHandler(service);

        final Mixer mixer = new Mixer(service, payoutHandler, scheduler);
        final MixerScheduler mixerScheduler = new MixerScheduler(scheduler);
        mixerScheduler.scheduleMixing(() -> mixer.mix(userOptions));

        System.out.printf("Please send your jobcoins to %s for mixing", mixer.getDepositAddress());
    }

    private static void print(final String str) {
        System.out.println("----------------------------------------------------------");
        System.out.println(str);
        System.out.println("----------------------------------------------------------");

    }
}
