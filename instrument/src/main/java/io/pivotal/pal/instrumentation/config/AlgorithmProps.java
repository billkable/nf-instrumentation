package io.pivotal.pal.instrumentation.config;


public class AlgorithmProps {
    private long highValue = 0L;
    private long lowValue = 0L;
    private long periodMs = 0L;
    private long startTimeMs = 0L;
    private long offPeriodMs = 0L;
    private double percentErrors = 0.0;

    public AlgorithmProps() {
        // Required for Config Properties mutable beans
    }

    public AlgorithmProps(long highValue,
                          long lowValue,
                          long periodMs,
                          long startTimeMs,
                          long offPeriodMs,
                          double percentErrors) {

        assertInvariants(highValue,
                        lowValue,
                        periodMs,
                        startTimeMs,
                        offPeriodMs,
                percentErrors);

        this.highValue = highValue;
        this.lowValue = lowValue;
        this.periodMs = periodMs;
        this.startTimeMs = startTimeMs;
        this.offPeriodMs = offPeriodMs;
        this.percentErrors = percentErrors;
    }

    private void assertInvariants(long highValue,
                                  long lowValue,
                                  long periodMs,
                                  long startTimeMs,
                                  long offPeriodMs,
                                  double percentile) {
        assertTemporalInvariants(periodMs,
                                startTimeMs,
                                offPeriodMs);
        assertRangeInvariants(highValue,
                                lowValue);
        assertPercentileInvariants(percentile);
    }

    private void assertTemporalInvariants(long periodMs,
                                          long startTimeMs,
                                          long offPeriodMs) {
        if (periodMs < 0)
            throw new IllegalArgumentException("periodMs must be " +
                    "greater than or equal to zero");

        if (startTimeMs < 0)
            throw new IllegalArgumentException("startTimeMs must be " +
                    "greater than or equal to zero");

        if (offPeriodMs < 0)
            throw new IllegalArgumentException("offPeriodMs must be " +
                    "equal to or greater than zero");

        if (offPeriodMs > periodMs) {
            throw new IllegalArgumentException("offPeriodMs must be " +
                    "less than or equal to periodMs");
        }
    }

    private void assertRangeInvariants(long highValue,
                                       long lowValue) {
        if (highValue < 0)
            throw new IllegalArgumentException("highValue must be " +
                    "equal to or greater than zero");

        if (lowValue < 0)
            throw new IllegalArgumentException("lowValue must be " +
                    "equal to or greater than zero");

        if (highValue < lowValue)
            throw new IllegalArgumentException("highValue must be " +
                    "greater than or the same as lowValue");
    }

    private void assertPercentileInvariants(double percentile) {
        if (percentile < 0 || percentile > 1.0)
            throw new IllegalArgumentException("percentErrors must be " +
                    "between zero and one");
    }

    public long getHighValue() {
        return highValue;
    }

    public void setHighValue(long highValue) {
        this.highValue = highValue;
    }

    public long getLowValue() {
        return lowValue;
    }

    public void setLowValue(long lowValue) {
        this.lowValue = lowValue;
    }

    public long getPeriodMs() {
        return periodMs;
    }

    public void setPeriodMs(long periodMs) {
        this.periodMs = periodMs;
    }

    public long getStartTimeMs() {
        return startTimeMs;
    }

    public void setStartTimeMs(long startTimeMs) {
        this.startTimeMs = startTimeMs;
    }

    public long getOffPeriodMs() {
        return offPeriodMs;
    }

    public void setOffPeriodMs(long offPeriodMs) {
        this.offPeriodMs = offPeriodMs;
    }

    public double getPercentErrors() {
        return percentErrors;
    }

    public void setPercentErrors(double percentErrors) {
        this.percentErrors = percentErrors;
    }

    public void validate() {
        assertInvariants(this.highValue,
                        this.lowValue,
                        this.periodMs,
                        this.startTimeMs,
                        this.offPeriodMs,
                        this.percentErrors);
    }
}
