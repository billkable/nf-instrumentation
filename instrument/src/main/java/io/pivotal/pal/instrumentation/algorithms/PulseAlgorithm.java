package io.pivotal.pal.instrumentation.algorithms;


import io.pivotal.pal.instrumentation.config.AlgorithmProps;

/**
 * PulseAlgorithm
 *
 * Based from a rectangle wave:
 *
 * `offPeriodMs` specifying the length of low range value
 * rest of period is at high range value
 */
public class PulseAlgorithm extends AbstractAlgorithm {
    public PulseAlgorithm(AlgorithmProps props) {
        super(props);
    }

    @Override
    public double getValue() {
        long startTime = getProps().getStartTimeMs();
        long period = getProps().getPeriodMs();
        long highValue = getProps().getHighValue();
        long lowValue = getProps().getLowValue();
        long timeStampMs = System.currentTimeMillis();
        long offPeriod = getProps().getOffPeriodMs();

        if (
                ((double)(timeStampMs - startTime)
                        % period) <
                        (period - offPeriod))
            return highValue;
        else return lowValue;
    }

}
