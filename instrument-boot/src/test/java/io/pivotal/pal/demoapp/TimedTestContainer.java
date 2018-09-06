package io.pivotal.pal.demoapp;

import io.pivotal.pal.instrumentation.InjectNfBehavior;
import org.springframework.stereotype.Component;

@Component
class TimedTestContainer {

    @InjectNfBehavior(cmdKey = "ss-latency")
    void executeConfiguredSteadyStateLatency() {
        // Do nothing - steady state latency will be injected
    }

    @InjectNfBehavior(name = "inlineMethod",
                                highValue = 500L)
    void executeInlineInstrumentedSteadyStateLatency() {
        // Do nothing - steady state latency will be injected
    }

    @InjectNfBehavior(cmdKey = "pulse-latency")
    void executeConfiguredPulseLatency() {
        // Do nothing - steady state latency will be injected
    }

    @InjectNfBehavior(cmdKey = "sine-latency")
    void executeConfiguredSineLatency() {
        // Do nothing - steady state latency will be injected
    }

    @InjectNfBehavior(cmdKey = "ramp-latency")
    void executeConfiguredRampLatency() {
        // Do nothing - steady state latency will be injected
    }

    @InjectNfBehavior(name = "errorMethod",
                                percentErrors = 1.0)
    void executeInstrumentedError() {
        // Do nothing - steady state latency will be injected
    }
}
