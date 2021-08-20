package com.mixer;

import com.mixer.models.UserOptions;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class MixerSchedulerTest {

    private MixerScheduler mixerScheduler;

    @Mock
    private ScheduledExecutorService mockScheduler;
    @Mock
    private Mixer mockMixer;
    private AutoCloseable openMocks;

    @BeforeEach
    public void setup() {
        this.openMocks = MockitoAnnotations.openMocks(this);
        this.mixerScheduler = new MixerScheduler(mockScheduler);
    }

    @Test
    @DisplayName("test scheduling of mixer")
    public void test_mixerScheduledWithOptions() {
        final UserOptions mockUserOptions = Mockito.mock(UserOptions.class);
        final Runnable runnableMix = () -> mockMixer.mix(mockUserOptions);

        mixerScheduler.scheduleMixing(runnableMix);

        Mockito.verify(mockScheduler).scheduleAtFixedRate(runnableMix, 0, 10, SECONDS);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

}
