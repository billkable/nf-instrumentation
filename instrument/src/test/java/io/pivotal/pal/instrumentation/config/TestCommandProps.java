package io.pivotal.pal.instrumentation.config;

import org.junit.Assert;
import org.junit.Test;

public class TestCommandProps {
    @Test
    public void testDefaultConfig() {

        CommandProps props = CommandProps.getDefaultProps();

        Assert.assertEquals(1000L, props.getHighValue());
        Assert.assertEquals(0L, props.getLowValue());
        Assert.assertEquals(0L, props.getPeriodMs());
        Assert.assertEquals(0L, props.getOffPeriodMs());
        Assert.assertEquals(0L, props.getStartTimeMs());
        Assert.assertEquals(0.0, props.getPercentErrors(),0.0);
    }

    @Test
    public void testTemporalInvariants() {
        CommandProps propsRangeViolation =
                CommandProps.override(
                        CommandProps.getDefaultProps(),
                        new CommandProps(null,null,null,null,null,null,
                                1000L,null)
                );

        try {
            propsRangeViolation.validate();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("offPeriodMs must be " +
                    "less than or equal to periodMs", e.getMessage());
        }

        CommandProps propsNegativeOffPeriodViolation =
                CommandProps.override(
                        CommandProps.getDefaultProps(),
                        new CommandProps(null,null,null,null,null,null,
                                -1L,null)
                );

        try {
            propsNegativeOffPeriodViolation.validate();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("offPeriodMs must be " +
                    "equal to or greater than zero", e.getMessage());
        }

        CommandProps propsNegativePeriodViolation =
                CommandProps.override(
                        CommandProps.getDefaultProps(),
                        new CommandProps(null,null,null,null,null,-1L,
                                null,null)
                );


        try {
            propsNegativePeriodViolation.validate();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("periodMs must be " +
                    "greater than or equal to zero", e.getMessage());
        }

        CommandProps propsTemporalValidation =
                CommandProps.override(
                        CommandProps.getDefaultProps(),
                        new CommandProps(null,null,null,null,null,10000L,
                                9000L,null)
                );

        Assert.assertEquals(10000L, propsTemporalValidation.getPeriodMs());
        Assert.assertEquals(9000L, propsTemporalValidation.getOffPeriodMs());
    }

    @Test
    public void testRangeInvariants() {

        CommandProps propsRangeFailureValidation =
                CommandProps.override(
                        CommandProps.getDefaultProps(),
                        new CommandProps(null,null,0L,500L,null,null,
                                null,null)
                );

        try {
            propsRangeFailureValidation.validate();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("highValue must be " +
                    "greater than or the same as lowValue",
                    e.getMessage());
        }

        CommandProps propsLowNegativeFailureValidation =
                CommandProps.override(
                        CommandProps.getDefaultProps(),
                        new CommandProps(null,null,null,-1L,null,null,
                                null,null)
                );

        try {
            propsLowNegativeFailureValidation.validate();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("lowValue must be " +
                    "equal to or greater than zero",
                    e.getMessage());
        }

        CommandProps propsHighNegativeFailureValidation =
                CommandProps.override(
                        CommandProps.getDefaultProps(),
                        new CommandProps(null,null,-1L,null,null,null,
                                null,null)
                );

        try {
            propsHighNegativeFailureValidation.validate();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("highValue must be " +
                    "equal to or greater than zero", e.getMessage());
        }

        CommandProps propsRangeValidation =
                CommandProps.override(
                        CommandProps.getDefaultProps(),
                        new CommandProps(null,null,1000L,100L,null,null,
                                null,null)
                );

        Assert.assertEquals(1000L, propsRangeValidation.getHighValue());
        Assert.assertEquals(100L, propsRangeValidation.getLowValue());
    }

    @Test
    public void testPercentErrorsInvariants() {

        CommandProps propsInvalidPercentErrorValidation =
                CommandProps.override(
                        CommandProps.getDefaultProps(),
                        new CommandProps(null,null,null,null,null,null,
                                null,-0.1)
                );


        try {
            propsInvalidPercentErrorValidation.validate();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("percentErrors must be " +
                            "between zero and one",
                            e.getMessage());
        }
    }
}
