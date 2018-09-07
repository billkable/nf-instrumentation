package io.pivotal.pal.instrumentation.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({BehaviorInstrumentConfig.class,
        io.pivotal.pal.instrumentation.config.CommandPropsEnvironmentSource.class})
public @interface EnableNfBehaviorInstrumentation {
}
