package io.pivotal.pal.instrumentation.config;

import org.junit.Assert;
import org.junit.Test;

public class TestAlgorithmProps {
    @Test
    public void testDefaultConfig() {

        AlgorithmProps props = new AlgorithmProps();

        Assert.assertEquals(0L, props.getHighValue());
        Assert.assertEquals(0L, props.getLowValue());
        Assert.assertEquals(0L, props.getPeriodMs());
        Assert.assertEquals(0L, props.getOffPeriodMs());
        Assert.assertEquals(0L, props.getStartTimeMs());
        Assert.assertEquals(0.0, props.getPercentErrors(),0.0);
    }

    @Test
    public void testTemporalInvariants() {
        AlgorithmProps props = new AlgorithmProps();

        try {
            props.setOffPeriodMs(1000L);
            props.validate();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("offPeriodMs must be " +
                    "less than or equal to periodMs", e.getMessage());
        }

        try {
            props.setOffPeriodMs(-1L);
            props.validate();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("offPeriodMs must be " +
                    "equal to or greater than zero", e.getMessage());
        }

        try {
            props.setPeriodMs(-1L);
            props.validate();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("periodMs must be " +
                    "greater than or equal to zero", e.getMessage());
        }

        props.setPeriodMs(10000L);
        props.setOffPeriodMs(9000L);

        Assert.assertEquals(10000L, props.getPeriodMs());
        Assert.assertEquals(9000L, props.getOffPeriodMs());
    }

    @Test
    public void testRangeInvariants() {

        AlgorithmProps props = new AlgorithmProps();

        try {
            props.setLowValue(500L);
            props.validate();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("highValue must be " +
                    "greater than or the same as lowValue",
                    e.getMessage());
        }

        try {
            props.setLowValue(-1L);
            props.validate();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("lowValue must be " +
                    "equal to or greater than zero",
                    e.getMessage());
        }

        try {
            props.setHighValue(-1L);
            props.validate();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("highValue must be " +
                    "equal to or greater than zero", e.getMessage());
        }

        props.setHighValue(1000L);
        props.setLowValue(100L);

        Assert.assertEquals(1000L, props.getHighValue());
        Assert.assertEquals(100L, props.getLowValue());
    }

    @Test
    public void testPercentileInvariants() {

        AlgorithmProps props = new AlgorithmProps();

        try {
            props.setPercentErrors(-0.1);
            props.validate();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("percentErrors must be " +
                            "between zero and one",
                            e.getMessage());
        }
    }
}
