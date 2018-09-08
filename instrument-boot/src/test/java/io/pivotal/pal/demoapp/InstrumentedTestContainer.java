package io.pivotal.pal.demoapp;

import io.pivotal.pal.instrumentation.InjectNfBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class InstrumentedTestContainer {
    private final Logger logger = LoggerFactory.getLogger(InstrumentedTestContainer.class);

    @InjectNfBehavior(pointCutName = "inlineMethod",
                        highValue = 500L)
    void executeInlineInstrumentedSteadyStateLatency() {
        logger.debug("Executing inline method");
    }

    @InjectNfBehavior(pointCutName = "configuredMethod")
    void executeConfiguredSteadyStateLatency() {
        logger.debug("Executing configured method");
    }

    @InjectNfBehavior(pointCutName = "configuredPulseMethod")
    void executeConfiguredPulseLatency() {
        logger.debug("Executing configured method");
    }

    @InjectNfBehavior(pointCutName = "errorMethod",
                                percentErrors = 1.0)
    void executeInstrumentedError() {
        logger.debug("Executing instrumented method");
    }
}
