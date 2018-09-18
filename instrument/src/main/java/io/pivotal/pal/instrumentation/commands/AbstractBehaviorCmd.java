package io.pivotal.pal.instrumentation.commands;

import io.pivotal.pal.instrumentation.algorithms.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

abstract class AbstractBehaviorCmd implements BehaviorCmd {
    private final Logger logger
            = LoggerFactory.getLogger(AbstractBehaviorCmd.class);

    private final Algorithm algorithm;
    private final Random random;

    AbstractBehaviorCmd(Algorithm algorithm) {
        this.algorithm = algorithm;
        this.random = new Random(1L);
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    boolean throwError() {
        boolean throwError = algorithm.getProps().getPercentErrors()
                > random.nextDouble();

        if (throwError) logger.debug("throwing algorithm runtime " +
                "exception in {}",
                algorithm.getProps().getCmdClass().getName());

        return throwError;
    }
}
