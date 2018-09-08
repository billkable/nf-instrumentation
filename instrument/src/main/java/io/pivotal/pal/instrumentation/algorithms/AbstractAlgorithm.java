package io.pivotal.pal.instrumentation.algorithms;

import io.pivotal.pal.instrumentation.config.CommandProps;

/**
 * AbstractAlgorithm
 *
 * basic implementation of a Temporal Algorithm that takes
 *
 * range of values, a time period over which to apply cyclic algorithm
 * with reference to a start time.
 */
public abstract class AbstractAlgorithm
    implements Algorithm {

    private final CommandProps props;

    AbstractAlgorithm(CommandProps props) {
        this.props = props;
    }

    public CommandProps getProps() {
        return this.props;
    }

}
