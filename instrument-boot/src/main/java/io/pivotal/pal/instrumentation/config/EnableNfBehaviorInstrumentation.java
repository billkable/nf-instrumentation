package io.pivotal.pal.instrumentation.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({BehaviorInstrumentConfig.class,
        io.pivotal.pal.instrumentation.config.props.PulseLatencyProps.class,
        io.pivotal.pal.instrumentation.config.props.PulseLatencyProps.class,
        io.pivotal.pal.instrumentation.config.props.RampLatencyProps.class,
        io.pivotal.pal.instrumentation.config.props.SineLatencyProps.class,
        io.pivotal.pal.instrumentation.config.props.SteadyStateLatencyProps.class})
public @interface EnableNfBehaviorInstrumentation {
}
