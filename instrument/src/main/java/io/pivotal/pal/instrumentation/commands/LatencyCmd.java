package io.pivotal.pal.instrumentation.commands;

import io.pivotal.pal.instrumentation.algorithms.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LatencyCmd extends AbstractBehaviorCmd {
    private final Logger logger
            = LoggerFactory.getLogger(LatencyCmd.class);

    public LatencyCmd(Algorithm algorithm) {
        super(algorithm);
    }

    @Override
    public void execute() {
        try {
            Thread.sleep(getLatency());
            if (throwError())
                throw new RuntimeException(
                        "Simulated Latency Runtime Exception");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private long getLatency() {
        long latency = Math.round(getAlgorithm().getValue());

        logger.info("generated latency {} ms", latency);
        return latency;
    }
}
