package io.pivotal.pal.instrumentation.algorithms;

import io.pivotal.pal.instrumentation.commands.BehaviorCmd;
import io.pivotal.pal.instrumentation.commands.LatencyCmd;
import io.pivotal.pal.instrumentation.config.CommandProps;
import org.junit.Assert;
import org.junit.Test;

public class TestSteadyStateAlgorithm {
    @Test
    public void testSteadyStateAlgorithm() {
        double value = 1000;

        SteadyStateAlgorithm algorithm
                = new SteadyStateAlgorithm(
                CommandProps.of()
                        .behavior(BehaviorCmd.class,
                                PulseAlgorithm.class)
                        .range(1000L,
                                0L)
                        .temporal(0L,
                                0L,
                                0L)
                        .percentErrors(0.0)
                        .build());

        Assert.assertEquals(value,algorithm.getValue(),0);
    }
}
