package io.pivotal.pal.instrumentation;

import io.pivotal.pal.instrumentation.algorithms.SteadyStateAlgorithm;
import io.pivotal.pal.instrumentation.commands.LatencyCmd;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InjectNfBehavior {
    String cmdKey() default "";
    String name() default "";
    Class cmdClass() default LatencyCmd.class;
    Class algorithmClass() default SteadyStateAlgorithm.class;
    long highValue() default 1000;
    long lowValue() default 0;
    long periodMs() default 0;
    long startTimeMs() default 0;
    long offPeriodMs() default 0;
    double percentErrors() default 0.0;
}
