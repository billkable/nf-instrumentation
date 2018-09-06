package io.pivotal.pal.instrumentation.algorithms;

import io.pivotal.pal.instrumentation.config.AlgorithmProps;

/**
 * SawtoothAlgorithm
 *
 * Basic temporal pattern modelling a Sawtooth
 */
public class SawtoothAlgorithm
        extends AbstractAlgorithm {

    public SawtoothAlgorithm(AlgorithmProps props) {
        super(props);
    }

    @Override
    public double getValue() {
        long startTime = getProps().getStartTimeMs();
        long period = getProps().getPeriodMs();
        long highValue = getProps().getHighValue();
        long lowValue = getProps().getLowValue();
        long timeStampMs = System.currentTimeMillis();

        return ((((double)(timeStampMs - startTime) % period)
                    /period) * (highValue - lowValue)
                ) + lowValue;
    }
}
