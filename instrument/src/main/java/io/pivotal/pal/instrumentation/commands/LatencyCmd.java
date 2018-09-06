package io.pivotal.pal.instrumentation.commands;

import io.pivotal.pal.instrumentation.algorithms.Algorithm;

public class LatencyCmd extends AbstractBehaviorCmd {

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
        return Math.round(getAlgorithm().getValue());
    }
}
