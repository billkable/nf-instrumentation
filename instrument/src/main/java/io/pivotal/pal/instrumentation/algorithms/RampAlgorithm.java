package io.pivotal.pal.instrumentation.algorithms;

import io.pivotal.pal.instrumentation.config.CommandProps;

/**
 * RampAlgorithm
 *
 * Basic temporal pattern modelling a Sawtooth
 */
public class RampAlgorithm
        extends AbstractAlgorithm {

    public RampAlgorithm(CommandProps props) {
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

        double rampValue =
                ((((double)(timeStampMs - startTime) % period)
                    /period) * (highValue - lowValue)
                ) + lowValue;

        if (
                ((double)(timeStampMs - startTime)
                        % period) <
                        (period - offPeriod))
            return rampValue;
        else return lowValue;
    }
}
