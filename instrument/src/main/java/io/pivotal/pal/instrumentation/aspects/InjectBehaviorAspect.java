package io.pivotal.pal.instrumentation.aspects;

import io.pivotal.pal.instrumentation.InjectNfBehavior;
import io.pivotal.pal.instrumentation.commands.BehaviorCmd;
import io.pivotal.pal.instrumentation.config.factories.CommandFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class InjectBehaviorAspect {
    private final Logger logger
            = LoggerFactory.getLogger(InjectBehaviorAspect.class);

    private CommandFactory commandFactory;

    public InjectBehaviorAspect(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    @Around("@annotation(io.pivotal.pal.instrumentation.InjectNfBehavior)")
    public Object injectLatency(ProceedingJoinPoint joinPoint)
            throws Throwable {

        InjectNfBehavior annotation =
                ((MethodSignature)joinPoint.getSignature())
                .getMethod()
                .getAnnotation(InjectNfBehavior.class);

        BehaviorCmd cmd = this.commandFactory.getCommand(annotation);

        logger.debug("processing non-functional behavior for {} ",
                annotation.pointCutName());
        cmd.execute();

        return joinPoint.proceed();
    }



}
