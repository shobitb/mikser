package com.mixer;

import com.mixer.models.Destination;
import com.mixer.models.UserOptions;
import com.mixer.service.JobCoinService;
import com.mixer.service.JobCoinServiceException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;

import static com.mixer.Constants.HOUSE_ADDRESS;
import static com.mixer.Constants.MIXER_FEE_ADDRESS;

public class MixerTest {
    private Mixer mixer;

    @Mock
    private JobCoinService mockJobCoinService;
    @Mock
    private ScheduledExecutorService mockScheduler;
    @Mock
    private PayoutHandler mockPayoutHandler;

    private AutoCloseable openMocks;

    @BeforeEach
    public void setup() {
        this.openMocks = MockitoAnnotations.openMocks(this);
        this.mixer = new Mixer(mockJobCoinService, mockPayoutHandler,
                mockScheduler);
    }

    @Test
    @DisplayName("test mixing without fees")
    public void depositTest_noFee() throws JobCoinServiceException {
        final BigDecimal deposit = BigDecimal.TEN;

        Mockito.when(mockJobCoinService.getBalance(mixer.getDepositAddress()))
                .thenReturn(deposit);

        final Destination d1 = new Destination("address_1", BigDecimal.ONE, 7);
        final UserOptions userOptions =
                new UserOptions(Collections.singletonList(d1), BigDecimal.ZERO);

        mixer.mix(userOptions);

        Mockito.verify(mockJobCoinService)
                .postTransaction(mixer.getDepositAddress(), HOUSE_ADDRESS, deposit);
        Mockito.verify(mockPayoutHandler)
                .createPayoutRunnable(
                        d1.getAddress(), deposit.multiply(d1.getDistribution()));
    }

    @Test
    @DisplayName("test mixing to multiple destinations, with fees")
    public void depositTest_multipleDestinations_withFee() throws JobCoinServiceException {
        final BigDecimal deposit = BigDecimal.TEN;
        final BigDecimal fee = BigDecimal.valueOf(0.005);

        Mockito.when(mockJobCoinService.getBalance(mixer.getDepositAddress()))
                .thenReturn(deposit);

        final Destination d1 = new Destination("address_1", BigDecimal.valueOf(0.2), 3);
        final Destination d2 = new Destination("address_2", BigDecimal.valueOf(0.2), 4);
        final Destination d3 = new Destination("address_3", BigDecimal.valueOf(0.5), 5);
        final Destination d4 = new Destination("address_4", BigDecimal.valueOf(0.1), 9);

        final UserOptions userOptions =
                new UserOptions(Arrays.asList(d1, d2, d3, d4), fee);

        mixer.mix(userOptions);

        final BigDecimal depositSansFee = deposit.subtract(deposit.multiply(fee));

        Mockito.verify(mockJobCoinService)
                .postTransaction(mixer.getDepositAddress(), HOUSE_ADDRESS, depositSansFee);

        Mockito.verify(mockPayoutHandler)
                .createPayoutRunnable(
                        d1.getAddress(), depositSansFee.multiply(d1.getDistribution()));
        Mockito.verify(mockPayoutHandler)
                .createPayoutRunnable(
                        d2.getAddress(), depositSansFee.multiply(d2.getDistribution()));
        Mockito.verify(mockPayoutHandler)
                .createPayoutRunnable(
                        d3.getAddress(), depositSansFee.multiply(d3.getDistribution()));
        Mockito.verify(mockPayoutHandler)
                .createPayoutRunnable(
                        d4.getAddress(), depositSansFee.multiply(d4.getDistribution()));
    }

    @Test
    @DisplayName("test fee payment")
    public void feeTest() throws JobCoinServiceException {
        final BigDecimal deposit = BigDecimal.TEN;
        final BigDecimal feePercent = BigDecimal.valueOf(0.01);

        Mockito.when(mockJobCoinService.getBalance(mixer.getDepositAddress()))
                .thenReturn(deposit);

        final Destination d1 = new Destination("address_1", BigDecimal.ONE, 7);
        final UserOptions userOptions =
                new UserOptions(Collections.singletonList(d1), feePercent);

        mixer.mix(userOptions);

        Mockito.verify(mockJobCoinService).postTransaction(mixer.getDepositAddress(), MIXER_FEE_ADDRESS,
                deposit.multiply(feePercent));
    }

    @Test
    @DisplayName("test no balance in deposit address")
    public void noDepositTest() throws JobCoinServiceException {
        final BigDecimal deposit = BigDecimal.ZERO;

        Mockito.when(mockJobCoinService.getBalance(mixer.getDepositAddress()))
                .thenReturn(deposit);

        final Destination d1 = new Destination("address_1", BigDecimal.ONE, 10);
        final UserOptions userOptions =
                new UserOptions(Collections.singletonList(d1), BigDecimal.ZERO);

        mixer.mix(userOptions);

        Mockito.verify(mockJobCoinService, Mockito.times(0))
                .postTransaction(mixer.getDepositAddress(), MIXER_FEE_ADDRESS, BigDecimal.ZERO);
        Mockito.verify(mockJobCoinService, Mockito.times(0))
                .postTransaction(mixer.getDepositAddress(), HOUSE_ADDRESS, deposit);
    }


    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }
}
