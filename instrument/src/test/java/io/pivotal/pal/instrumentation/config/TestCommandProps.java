package io.pivotal.pal.instrumentation.config;

import io.pivotal.pal.instrumentation.algorithms.SteadyStateAlgorithm;
import io.pivotal.pal.instrumentation.commands.LatencyCmd;
import org.junit.Assert;
import org.junit.Test;

public class TestCommandProps {
    @Test
    public void testDefaultConfig() {

        CommandProps props = CommandProps.of()
                                    .defaultCommand()
                                    .build();

        Assert.assertEquals(1000L, props.getHighValue());
        Assert.assertEquals(0L, props.getLowValue());
        Assert.assertEquals(0L, props.getPeriodMs());
        Assert.assertEquals(0L, props.getOffPeriodMs());
        Assert.assertEquals(0L, props.getStartTimeMs());
        Assert.assertEquals(0.0, props.getPercentErrors(),0.0);
    }

    @Test
    public void testTemporalInvariants() {
        try {
            CommandProps.override(
                    CommandProps.of()
                            .defaultCommand()
                            .build(),
                    CommandProps.of()
                            .offPeriodMs(1000L)
                            .buildRaw()
            );

            Assert.fail();

        } catch (IllegalArgumentException e) {
            Assert.assertEquals("offPeriodMs must be " +
                    "less than or equal to periodMs", e.getMessage());
        }

        try {
            CommandProps.override(
                    CommandProps.of()
                            .defaultCommand()
                            .build(),
                    CommandProps.of()
                            .offPeriodMs(-1L)
                            .buildRaw()
            );

            Assert.fail();

        } catch (IllegalArgumentException e) {
            Assert.assertEquals("offPeriodMs must be " +
                    "equal to or greater than zero", e.getMessage());
        }

        try {
            CommandProps.override(
                    CommandProps.of()
                            .defaultCommand()
                            .build(),
                    CommandProps.of()
                            .periodMs(-1L)
                            .buildRaw()
            );

            Assert.fail();

        } catch (IllegalArgumentException e) {
            Assert.assertEquals("periodMs must be " +
                    "greater than or equal to zero", e.getMessage());
        }

        CommandProps propsTemporalValidation =
                CommandProps.override(
                        CommandProps.of()
                                .defaultCommand()
                                .build(),
                        CommandProps.of()
                                .defaultCommand()
                                .temporal(0L,
                                        10000L,
                                        9000L)
                                .buildRaw()
                );

        Assert.assertEquals(10000L, propsTemporalValidation.getPeriodMs());
        Assert.assertEquals(9000L, propsTemporalValidation.getOffPeriodMs());
    }

    @Test
    public void testRangeInvariants() {

        try {
            CommandProps.override(
                    CommandProps.of()
                            .defaultCommand()
                            .build(),
                    CommandProps.of()
                            .defaultCommand()
                            .highValue(0L)
                            .lowValue(500L)
                            .buildRaw()
            );

            Assert.fail();

        } catch (IllegalArgumentException e) {
            Assert.assertEquals("highValue must be " +
                    "greater than or the same as lowValue",
                    e.getMessage());
        }

        try {
            CommandProps.override(
                    CommandProps.of()
                            .defaultCommand()
                            .build(),
                    CommandProps.of()
                            .defaultCommand()
                            .lowValue(-1L)
                            .buildRaw()
            );

            Assert.fail();

        } catch (IllegalArgumentException e) {
            Assert.assertEquals("lowValue must be " +
                    "equal to or greater than zero",
                    e.getMessage());
        }

        try {
            CommandProps.override(
                    CommandProps.of()
                            .defaultCommand()
                            .build(),
                    CommandProps.of()
                            .defaultCommand()
                            .highValue(-1L)
                            .buildRaw()
            );

            Assert.fail();

        } catch (IllegalArgumentException e) {
            Assert.assertEquals("highValue must be " +
                    "equal to or greater than zero", e.getMessage());
        }

        CommandProps propsRangeValidation =
                CommandProps.override(
                        CommandProps.of()
                                .defaultCommand()
                                .build(),
                        CommandProps.of()
                                .defaultCommand()
                                .range(1000L,
                                        100L)
                                .buildRaw()
                );

        Assert.assertEquals(1000L, propsRangeValidation.getHighValue());
        Assert.assertEquals(100L, propsRangeValidation.getLowValue());
    }

    @Test
    public void testPercentErrorsInvariants() {

        try {
            CommandProps.override(
                    CommandProps.of()
                            .defaultCommand()
                            .build(),
                    CommandProps.of()
                            .defaultCommand()
                            .percentErrors(-0.1)
                            .buildRaw()
            );

            Assert.fail();

        } catch (IllegalArgumentException e) {
            Assert.assertEquals("percentErrors must be " +
                            "between zero and one",
                            e.getMessage());
        }
    }

    @Test
    public void testBuilder() {
        CommandProps props = CommandProps.of()
                .behavior(  LatencyCmd.class,
                            SteadyStateAlgorithm.class)
                .temporal(100L,
                            30000L,
                            10000L)
                .range(1000L,500L)
                .percentErrors(0.2)
                .build();

        Assert.assertEquals(LatencyCmd.class,props.getCmdClass());
        Assert.assertEquals(SteadyStateAlgorithm.class,props.getAlgorithmClass());
        Assert.assertEquals(100L,props.getStartTimeMs());

        Assert.assertEquals(30000L,props.getPeriodMs());
        Assert.assertEquals(10000L,props.getOffPeriodMs());

        Assert.assertEquals(0.2,props.getPercentErrors(),0.0);
    }
}
