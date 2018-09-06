package io.pivotal.pal.instrumentation.aspects;

import io.pivotal.pal.instrumentation.InjectNfBehavior;
import io.pivotal.pal.instrumentation.commands.BehaviorCmd;
import io.pivotal.pal.instrumentation.config.factories.CommandFactory;
import io.pivotal.pal.instrumentation.config.factories.StaticCommandFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class InjectBehaviorAspect {
    private final StaticCommandFactory commandFactory;

    public InjectBehaviorAspect(CommandFactory dynamicProcessor) {
        this.commandFactory = new StaticCommandFactory(
                dynamicProcessor
        );
    }

    @Around("@annotation(io.pivotal.pal.instrumentation.InjectNfBehavior)")
    public Object injectLatency(ProceedingJoinPoint joinPoint)
            throws Throwable {

        InjectNfBehavior annotation =
                ((MethodSignature)joinPoint.getSignature())
                .getMethod()
                .getAnnotation(InjectNfBehavior.class);

        BehaviorCmd cmd = this.commandFactory.getCommand(annotation);

        cmd.execute();

        return joinPoint.proceed();
    }



}
