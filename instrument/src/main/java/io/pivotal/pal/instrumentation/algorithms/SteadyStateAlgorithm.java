package io.pivotal.pal.instrumentation.algorithms;

import io.pivotal.pal.instrumentation.config.CommandProps;

/**
 * SteadyStateAlgorithm
 *
 * Always return same value
 */
public class SteadyStateAlgorithm extends AbstractAlgorithm {

    public SteadyStateAlgorithm(CommandProps props) {
        super(props);
    }

    @Override
    public double getValue() {
        return getProps().getHighValue();
    }
}
