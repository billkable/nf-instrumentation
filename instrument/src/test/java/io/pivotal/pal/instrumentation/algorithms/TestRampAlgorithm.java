package io.pivotal.pal.instrumentation.algorithms;

import io.pivotal.pal.instrumentation.commands.LatencyCmd;
import io.pivotal.pal.instrumentation.config.CommandProps;
import org.junit.Assert;
import org.junit.Test;

public class TestRampAlgorithm {
    private static final long PERIOD = 10000;

    private static final double MIN_VALUE = 0;
    private static final double TWENTY_FIFTH_VALUE = .25;
    private static final double MEDIAN_VALUE = 0.5;
    private static final double SEVENTY_FIFTH_VALUE = .75;
    private static final long MAX_VALUE = 1;

    @Test
    public void testAlgorithm() {
        long startTime = System.currentTimeMillis()
                - (3 * PERIOD + 2500);

        testValue(startTime, TWENTY_FIFTH_VALUE);
    }

    @Test
    public void testAlgorithmSyncPeriod() {
        long startTime = System.currentTimeMillis() - (3 * PERIOD);

        testValue(startTime, MIN_VALUE);
    }

    @Test
    public void testAlgorithmWithinSamePeriod() {
        long startTime = System.currentTimeMillis() - 5000;

        testValue(startTime, MEDIAN_VALUE);
    }

    @Test
    public void testAlgorithm75th() {
        long startTime = System.currentTimeMillis() -
                ((3 * PERIOD) + 7500);

        testValue(startTime, SEVENTY_FIFTH_VALUE);
    }

    @Test
    public void testAlgorithmMax() {
        long startTime = System.currentTimeMillis() -
                ((3 * PERIOD) + 9999);

        testValue(startTime, MAX_VALUE);
    }

    private void testValue(long startTime, double expectedValue) {
        long lowValue = 0;
        long highValue = 1;

        RampAlgorithm algorithm = new RampAlgorithm(
                new CommandProps(
                        LatencyCmd.class,
                        RampAlgorithm.class,
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