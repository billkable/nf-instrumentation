package io.pivotal.pal.instrumentation.commands;

import io.pivotal.pal.instrumentation.algorithms.Algorithm;

import java.util.Random;

abstract class AbstractBehaviorCmd implements BehaviorCmd {
    private final Algorithm algorithm;
    private final Random random;

    AbstractBehaviorCmd(Algorithm algorithm) {
        this.algorithm = algorithm;
        this.random = new Random(1L);
    }

    Algorithm getAlgorithm() {
        return algorithm;
    }

    boolean throwError() {
        return algorithm.getProps().getPercentErrors()
                > random.nextDouble();
    }
}
