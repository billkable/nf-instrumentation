package io.pivotal.pal.instrumentation.config;

import io.pivotal.pal.instrumentation.algorithms.PulseAlgorithm;
import io.pivotal.pal.instrumentation.algorithms.SawtoothAlgorithm;
import io.pivotal.pal.instrumentation.algorithms.SineAlgorithm;
import io.pivotal.pal.instrumentation.algorithms.SteadyStateAlgorithm;
import io.pivotal.pal.instrumentation.aspects.InjectBehaviorAspect;
import io.pivotal.pal.instrumentation.commands.BehaviorCmd;
import io.pivotal.pal.instrumentation.commands.LatencyCmd;
import io.pivotal.pal.instrumentation.config.props.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BehaviorInstrumentConfig {
    private final ApplicationContext ctx;
    private final SteadyStateLatencyProps steadyStateLatencyProps;
    private final PulseLatencyProps pulseLatencyProps;
    private final SineLatencyProps sineLatencyProps;
    private final RampLatencyProps rampLatencyProps;

    public BehaviorInstrumentConfig(ApplicationContext ctx,
                                    SteadyStateLatencyProps steadyStateLatencyProps,
                                    PulseLatencyProps pulseLatencyProps,
                                    SineLatencyProps sineLatencyProps,
                                    RampLatencyProps rampLatencyProps) {

        this.ctx = ctx;
        this.steadyStateLatencyProps = steadyStateLatencyProps;
        this.pulseLatencyProps = pulseLatencyProps;
        this.sineLatencyProps = sineLatencyProps;
        this.rampLatencyProps = rampLatencyProps;
    }

    @Bean
    public InjectBehaviorAspect injectBehaviorAspect() {
        return new InjectBehaviorAspect(
                key -> (BehaviorCmd)ctx.getBean(key.toString()));
    }

    @RefreshScope
    @Bean(name = "ss-latency")
    @ConditionalOnProperty(prefix = "ss-latency.command",
            name = "highValue")
    public BehaviorCmd ssLatency() {
        steadyStateLatencyProps.validate();
        return new LatencyCmd(
                new SteadyStateAlgorithm(steadyStateLatencyProps));

    }

    @RefreshScope
    @Bean(name = "pulse-latency")
    @ConditionalOnProperty(prefix = "pulse-latency.command",
                            name = "highValue")
    public BehaviorCmd pulseLatency() {
        pulseLatencyProps.validate();
        return new LatencyCmd(
                new PulseAlgorithm(pulseLatencyProps));
    }

    @RefreshScope
    @Bean(name = "sine-latency")
    @ConditionalOnProperty(prefix = "sine-latency.command",
            name = "highValue")
    public BehaviorCmd sineLatency() {
        sineLatencyProps.validate();
        return new LatencyCmd(
                new SineAlgorithm(sineLatencyProps));
    }

    @RefreshScope
    @Bean(name = "ramp-latency")
    @ConditionalOnProperty(prefix = "ramp-latency.command",
            name = "highValue")
    public BehaviorCmd sawLatency() {
        rampLatencyProps.validate();
        return new LatencyCmd(
                new SawtoothAlgorithm(rampLatencyProps));
    }
}
