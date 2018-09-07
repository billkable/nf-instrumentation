package io.pivotal.pal.instrumentation.algorithms;

import io.pivotal.pal.instrumentation.config.CommandProps;

/**
 * Interface for defining time base algorithm of
 *
 *  `f(t)` where `t` is the current time of `getValue()` execution.
 */
public interface Algorithm {
    double getValue();
    CommandProps getProps();
}
