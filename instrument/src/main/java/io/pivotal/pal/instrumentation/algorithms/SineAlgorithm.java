package io.pivotal.pal.instrumentation.algorithms;

import io.pivotal.pal.instrumentation.config.AlgorithmProps;

/**
 * SineAlgorithm
 *
 * Basic temporal pattern modelling a Sawtooth
 */
public class SineAlgorithm
        extends AbstractAlgorithm {

    public SineAlgorithm(AlgorithmProps props) {
        super(props);
    }

    @Override
    public double getValue() {
        long startTime = getProps().getStartTimeMs();
        long period = getProps().getPeriodMs();
        long highValue = getProps().getHighValue();
        long lowValue = getProps().getLowValue();
        long timeStampMs = System.currentTimeMillis();

        return (((Math.sin(Math.PI * 2 *
                ((double)(timeStampMs - startTime) % period/period)))
                + 1) / 2
                * (highValue - lowValue))
                + lowValue;
    }
}
