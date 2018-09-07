package io.pivotal.pal.instrumentation.algorithms;

import io.pivotal.pal.instrumentation.commands.LatencyCmd;
import io.pivotal.pal.instrumentation.config.CommandProps;
import org.junit.Assert;
import org.junit.Test;

public class TestSineAlgorithm {
    private static final long PERIOD = 10000;
    private static final long MAX_VALUE = 1;
    private static final double MEDIAN_VALUE = 0.5;
    private static final long MIN_VALUE = 0;

    @Test
    public void testAlgorithm() {
        long startTime = System.currentTimeMillis()
                - (3 * PERIOD + 2500);

        testValue(startTime, (double) MAX_VALUE);
    }

    @Test
    public void testAlgorithmSyncPeriod() {
        long startTime = System.currentTimeMillis() - (3 * PERIOD);

        testValue(startTime, MEDIAN_VALUE);
    }

    @Test
    public void testAlgorithmWithinSamePeriod() {
        long startTime = System.currentTimeMillis() - 2500;

        testValue(startTime, (double) MAX_VALUE);
    }

    @Test
    public void testAlgorithmLowPoint() {
        long startTime = System.currentTimeMillis() -
                ((3 * PERIOD) + 7500);

        testValue(startTime, (double) MIN_VALUE);
    }

    private void testValue(long startTime, double expectedValue) {
        long lowValue = 0;
        long highValue = 1;

        SineAlgorithm algorithm = new SineAlgorithm(
                new CommandProps(
                        LatencyCmd.class,
                        SineAlgorithm.class,
                        highValue,
                        lowValue,
                        startTime,
                        PERIOD,
                        0L,
                        0.0
                )
        );

        Assert.assertEquals(expectedValue,
                algorithm.getValue(),
                .001);
    }
}
