package io.pivotal.pal.instrumentation.algorithms;

import io.pivotal.pal.instrumentation.commands.BehaviorCmd;
import io.pivotal.pal.instrumentation.commands.LatencyCmd;
import io.pivotal.pal.instrumentation.config.CommandProps;
import org.junit.Assert;
import org.junit.Test;

public class TestPulseAlgorithm {
    private static final long PERIOD = 10000;
    private static final long LATCH_OFF_PERIOD = 6000;
    private static final long MAX_VALUE = 1;
    private static final long MIN_VALUE = 0;

    @Test
    public void testLatchAlgorithm() {
        long startTime = System.currentTimeMillis()
            - (3 * PERIOD + 2500);

        testCycleValue(startTime, MAX_VALUE);
    }

    @Test
    public void testCycleAlgorithmWithinSamePeriod() {
        long startTime = System.currentTimeMillis() - 2500;

        testCycleValue(startTime, MAX_VALUE);
    }

    @Test
    public void testCycleAlgorithmLatchOffPoint() {
        long startTime = System.currentTimeMillis()
        - (3 * PERIOD + 7500);

        testCycleValue(startTime, MIN_VALUE);
    }

    private void testCycleValue(long startTime, long expectedValue) {
        long lowValue = 0;
        long highValue = 1;

        PulseAlgorithm algorithm = new PulseAlgorithm(
                CommandProps.of()
                        .behavior(BehaviorCmd.class,
                                PulseAlgorithm.class)
                        .range(highValue,
                                lowValue)
                        .temporal(startTime,
                                PERIOD,
                                LATCH_OFF_PERIOD)
                        .percentErrors(0.0)
                        .build());

        Assert.assertEquals(expectedValue,
                algorithm.getValue(),.001);

    }
}