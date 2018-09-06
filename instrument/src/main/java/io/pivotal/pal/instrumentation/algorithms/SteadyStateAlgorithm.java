package io.pivotal.pal.instrumentation.algorithms;

import io.pivotal.pal.instrumentation.config.AlgorithmProps;

/**
 * SteadyStateAlgorithm
 *
 * Always return same value
 */
public class SteadyStateAlgorithm extends AbstractAlgorithm {

    public SteadyStateAlgorithm(AlgorithmProps props) {
        super(props);
    }

    @Override
    public double getValue() {
        return getProps().getHighValue();
    }
}
