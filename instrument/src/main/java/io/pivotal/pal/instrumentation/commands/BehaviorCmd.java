package io.pivotal.pal.instrumentation.commands;

import io.pivotal.pal.instrumentation.algorithms.Algorithm;

public interface BehaviorCmd {
    void execute();
    Algorithm getAlgorithm();
}
