package io.pivotal.pal.instrumentation.commands;

import io.pivotal.pal.instrumentation.algorithms.SteadyStateAlgorithm;
import io.pivotal.pal.instrumentation.config.CommandProps;
import org.junit.Assert;
import org.junit.Test;

public class TestLatencyCmd {
    @Test
    public void testLatencyCmd() {
        LatencyCmd cmd = new LatencyCmd(
                new SteadyStateAlgorithm(
                        CommandProps.of()
                                .defaultCommand()
                                .range(100L,0L)
                                .build()
                )
        );

        long startTime = System.currentTimeMillis();
        cmd.execute();

        long executionTime = System.currentTimeMillis() - startTime;

        Assert.assertTrue("Sleep out of bounds",
                executionTime >= 100 && executionTime < 110);
    }
}
