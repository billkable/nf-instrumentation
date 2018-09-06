package io.pivotal.pal.instrumentation.algorithms;

import io.pivotal.pal.instrumentation.config.AlgorithmProps;

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

    private final AlgorithmProps props;

    AbstractAlgorithm(AlgorithmProps props) {
        props.validate();
        this.props = props;
    }

    public AlgorithmProps getProps() {
        return this.props;
    }

}
