package io.pivotal.pal.demoapp;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InstrumentBehaviorApp.class)
public class InstrumentBehaviorAppTests {

    @Autowired
    private TimedTestContainer container;

    @Test
    public void testConfiguredSteadyStateLatency() {
        long startTime = System.currentTimeMillis();

        container.executeConfiguredSteadyStateLatency();

        long executionTime = System.currentTimeMillis() - startTime;

        Assert.assertEquals(500.0, (double)executionTime,
                50);
    }

    @Test
    public void testInstrumentedSteadyStateLatency() {
        long startTime = System.currentTimeMillis();

        container.executeInlineInstrumentedSteadyStateLatency();

        long executionTime = System.currentTimeMillis() - startTime;

        Assert.assertEquals(500.0, (double)executionTime,
                50.0);
    }

    @Test
    public void testConfiguredPulseLatency() {
        long startTime = System.currentTimeMillis();

        container.executeConfiguredPulseLatency();

        long executionTime = System.currentTimeMillis() - startTime;

        Assert.assertEquals(500.0, (double)executionTime,
                50.0);
    }

    @Test
    public void testConfiguredSineLatency() {
        long startTime = System.currentTimeMillis();

        container.executeConfiguredSineLatency();

        long executionTime = System.currentTimeMillis() - startTime;

        Assert.assertEquals(500.0, (double)executionTime,
                50.0);
    }

    @Test
    public void testConfiguredRampLatency() {
        long startTime = System.currentTimeMillis();

        container.executeConfiguredRampLatency();

        long executionTime = System.currentTimeMillis() - startTime;

        Assert.assertEquals(500.0, (double)executionTime,
                50.0);
    }

    @Test
    public void testInstrumentedError() {
        try {
            container.executeInstrumentedError();
        } catch (RuntimeException e) {
            Assert.assertEquals(
                    "Simulated Latency Runtime Exception",
                    e.getMessage());
        }
    }
}
