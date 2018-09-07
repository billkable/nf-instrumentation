package io.pivotal.pal.instrumentation.config;


import io.pivotal.pal.instrumentation.algorithms.Algorithm;
import io.pivotal.pal.instrumentation.algorithms.SteadyStateAlgorithm;
import io.pivotal.pal.instrumentation.commands.BehaviorCmd;
import io.pivotal.pal.instrumentation.commands.LatencyCmd;

public class CommandProps {
    private final Class<BehaviorCmd> cmdClass;
    private final Class<Algorithm> algorithmClass;
    private final Long highValue;
    private final Long lowValue;
    private final Long startTimeMs;
    private final Long periodMs;
    private final Long offPeriodMs;
    private final Double percentErrors;

    public CommandProps(CommandProps props) {
        this.cmdClass = props.cmdClass;
        this.algorithmClass = props.algorithmClass;
        this.highValue = props.highValue;
        this.lowValue = props.lowValue;
        this.startTimeMs = props.startTimeMs;
        this.periodMs = props.periodMs;
        this.offPeriodMs = props.offPeriodMs;
        this.percentErrors = props.percentErrors;
    }

    public static CommandProps getDefaultProps() {
        return new CommandProps(
            LatencyCmd.class,
            SteadyStateAlgorithm.class,
            1000L,
            0L,
            0L,
            0L,
            0L,
            0.0
        );
    }

    public static CommandProps override(CommandProps sourceProps,
                                        CommandProps targetProps) {
        CommandProps newTarget = new CommandProps(
            targetProps.cmdClass == null ?
                    sourceProps.cmdClass : targetProps.cmdClass,
            targetProps.algorithmClass == null ?
                    sourceProps.algorithmClass : targetProps.algorithmClass,
            targetProps.highValue == null ?
                    sourceProps.highValue : targetProps.highValue,
            targetProps.lowValue == null ?
                    sourceProps.lowValue : targetProps.lowValue,
            targetProps.startTimeMs == null ?
                    sourceProps.startTimeMs : targetProps.startTimeMs,
            targetProps.periodMs == null ?
                    sourceProps.periodMs : targetProps.periodMs,
            targetProps.offPeriodMs == null ?
                    sourceProps.offPeriodMs : targetProps.offPeriodMs,
            targetProps.percentErrors == null ?
                    sourceProps.percentErrors : targetProps.percentErrors
        );

        return newTarget;
    }

    public CommandProps(Class cmdClass,
                        Class algorithmClass,
                        Long highValue,
                        Long lowValue,
                        Long startTimeMs,
                        Long periodMs,
                        Long offPeriodMs,
                        Double percentErrors) {

        this.cmdClass = cmdClass;
        this.algorithmClass = algorithmClass;
        this.highValue = highValue;
        this.lowValue = lowValue;
        this.periodMs = periodMs;
        this.startTimeMs = startTimeMs;
        this.offPeriodMs = offPeriodMs;
        this.percentErrors = percentErrors;
    }

    private void assertInvariants(  Class cmdClass,
                                    Class algorithmClass,
                                    long highValue,
                                    long lowValue,
                                    long startTimeMs,
                                    long periodMs,
                                    long offPeriodMs,
                                    double percentile) {
        assertClasses(cmdClass,algorithmClass);

        assertTemporalInvariants(periodMs,
                                startTimeMs,
                                offPeriodMs);

        assertRangeInvariants(highValue,
                                lowValue);

        assertPercentileInvariants(percentile);
    }

    private void assertClasses(Class cmdClass,
                               Class algorithmClass) {
        if (cmdClass == null)
            throw new IllegalArgumentException
                    ("cmdClass is not configured");

        if (algorithmClass == null)
            throw new IllegalArgumentException
                    ("algorithmClass is not configured");
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

    public Class<BehaviorCmd> getCmdClass() {
        return cmdClass;
    }

    public Class<Algorithm> getAlgorithmClass() {
        return algorithmClass;
    }

    public long getHighValue() {
        return highValue;
    }

    public long getLowValue() {
        return lowValue;
    }

    public long getPeriodMs() {
        return periodMs;
    }

    public long getStartTimeMs() {
        return startTimeMs;
    }

    public long getOffPeriodMs() {
        return offPeriodMs;
    }

    public double getPercentErrors() {
        return percentErrors;
    }

    public void validate() {
        assertInvariants(this.cmdClass,
                        this.algorithmClass,
                        this.highValue,
                        this.lowValue,
                        this.startTimeMs,
                        this.periodMs,
                        this.offPeriodMs,
                        this.percentErrors);
    }
}
