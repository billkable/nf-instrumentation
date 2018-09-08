package io.pivotal.pal.instrumentation.config;

import io.pivotal.pal.instrumentation.algorithms.Algorithm;
import io.pivotal.pal.instrumentation.algorithms.SteadyStateAlgorithm;
import io.pivotal.pal.instrumentation.commands.BehaviorCmd;
import io.pivotal.pal.instrumentation.commands.LatencyCmd;

public class CommandProps {
    private Class<BehaviorCmd> cmdClass;
    private Class<Algorithm> algorithmClass;
    private Long highValue;
    private Long lowValue;
    private Long startTimeMs;
    private Long periodMs;
    private Long offPeriodMs;
    private Double percentErrors;

    private CommandProps() {}

    public static class Builder {
        private CommandProps props = new CommandProps();

        public Builder defaultCommand() {
            behavior(LatencyCmd.class, SteadyStateAlgorithm.class);
            range( 1000L, 0L);
            temporal(0L,0L,0L);
            percentErrors(0.0);

            return this;
        }

        public Builder command(Class cmdClass) {
            props.cmdClass = cmdClass;

            return this;
        }

        public Builder algorithm(Class algorithmClass) {
            props.algorithmClass = algorithmClass;

            return this;
        }

        public Builder highValue(Long highValue) {
            props.highValue = highValue;
            return this;
        }

        public Builder lowValue(Long lowValue) {
            props.lowValue = lowValue;
            return this;
        }

        public Builder startTimeMs(Long startTimeMs) {
            props.startTimeMs = startTimeMs;
            return this;
        }

        public Builder periodMs(Long periodMs) {
            props.periodMs = periodMs;
            return this;
        }

        public Builder offPeriodMs(Long offPeriodMs) {
            props.offPeriodMs = offPeriodMs;
            return this;
        }

        public Builder percentErrors(Double percentErrors) {
            props.percentErrors = percentErrors;
            return this;
        }

        public Builder behavior(Class cmdClass,
                                Class algorithmClass) {
            command(cmdClass);
            algorithm(algorithmClass);

            return this;
        }

        public Builder range(Long highValue,
                             Long lowValue) {

            highValue(highValue);
            lowValue(lowValue);
            return this;
        }

        public Builder temporal(Long startTimeMs,
                                Long periodMs,
                                Long offPeriodMs) {

            startTimeMs(startTimeMs);
            periodMs(periodMs);
            offPeriodMs(offPeriodMs);

            return this;
        }

        public CommandProps buildRaw() {
            return this.props;
        }

        public CommandProps build() {
            props.validate();
            return buildRaw();
        }
    }

    public static Builder of() {
        return new Builder();
    }

    public static CommandProps override(CommandProps sourceProps,
                                        CommandProps targetProps) {
        return of()
                .behavior(targetProps.cmdClass == null ?
                                sourceProps.cmdClass :
                                targetProps.cmdClass,
                        targetProps.algorithmClass == null ?
                                sourceProps.algorithmClass :
                                targetProps.algorithmClass)
                .range(targetProps.highValue == null ?
                                sourceProps.highValue :
                                targetProps.highValue,
                        targetProps.lowValue == null ?
                                sourceProps.lowValue :
                                targetProps.lowValue)
                .temporal(targetProps.startTimeMs == null ?
                                sourceProps.startTimeMs :
                                targetProps.startTimeMs,
                        targetProps.periodMs == null ?
                                sourceProps.periodMs :
                                targetProps.periodMs,
                        targetProps.offPeriodMs == null ?
                                sourceProps.offPeriodMs :
                                targetProps.offPeriodMs)
                .percentErrors(targetProps.percentErrors == null ?
                                sourceProps.percentErrors :
                                targetProps.percentErrors)
                .build();
    }
    
    private void assertInvariants(  Class cmdClass,
                                    Class algorithmClass,
                                    Long highValue,
                                    Long lowValue,
                                    Long startTimeMs,
                                    Long periodMs,
                                    Long offPeriodMs,
                                    Double percentile) {
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

    private void assertTemporalInvariants(Long periodMs,
                                          Long startTimeMs,
                                          Long offPeriodMs) {
        if (null == periodMs || periodMs < 0)
            throw new IllegalArgumentException("periodMs must be " +
                    "greater than or equal to zero");

        if (null == startTimeMs || startTimeMs < 0)
            throw new IllegalArgumentException("startTimeMs must be " +
                    "greater than or equal to zero");

        if (null == offPeriodMs || offPeriodMs < 0)
            throw new IllegalArgumentException("offPeriodMs must be " +
                    "equal to or greater than zero");

        if (offPeriodMs > periodMs) {
            throw new IllegalArgumentException("offPeriodMs must be " +
                    "less than or equal to periodMs");
        }
    }

    private void assertRangeInvariants(Long highValue,
                                       Long lowValue) {
        if (null == highValue || highValue < 0)
            throw new IllegalArgumentException("highValue must be " +
                    "equal to or greater than zero");

        if (null == lowValue || lowValue < 0)
            throw new IllegalArgumentException("lowValue must be " +
                    "equal to or greater than zero");

        if (highValue < lowValue)
            throw new IllegalArgumentException("highValue must be " +
                    "greater than or the same as lowValue");
    }

    private void assertPercentileInvariants(Double percentile) {
        if (null == percentile || percentile < 0 || percentile > 1.0)
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

    private void validate() {
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
