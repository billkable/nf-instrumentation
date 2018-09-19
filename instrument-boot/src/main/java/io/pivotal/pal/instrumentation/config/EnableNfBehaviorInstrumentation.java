package io.pivotal.pal.instrumentation.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({BehaviorInstrumentConfig.class,
        CommandPropsEnvironmentSource.class,
        BehaviorCmdEndpoint.class})
public @interface EnableNfBehaviorInstrumentation {
}
