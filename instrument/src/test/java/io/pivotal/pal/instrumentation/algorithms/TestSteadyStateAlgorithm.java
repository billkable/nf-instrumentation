package io.pivotal.pal.instrumentation.algorithms;

import io.pivotal.pal.instrumentation.config.AlgorithmProps;
import org.junit.Assert;
import org.junit.Test;

public class TestSteadyStateAlgorithm {
    @Test
    public void testSteadyStateAlgorithm() {
        double value = 1000;

        SteadyStateAlgorithm algorithm
                = new SteadyStateAlgorithm(
                        new AlgorithmProps(
                                1000L,
                                0L,
                                0L,
                                0L,
                                0L,
                                0.0
                        )
        );

        Assert.assertEquals(value,algorithm.getValue(),0);
    }
}
