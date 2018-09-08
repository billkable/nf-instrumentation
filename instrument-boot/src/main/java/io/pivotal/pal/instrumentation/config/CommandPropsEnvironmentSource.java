package io.pivotal.pal.instrumentation.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class CommandPropsEnvironmentSource implements CommandPropsSource {
    private final Logger logger = LoggerFactory.getLogger(CommandPropsEnvironmentSource.class);

    private final Environment environment;

    public CommandPropsEnvironmentSource(Environment environment) {
        this.environment = environment;
    }

    public CommandProps getProps(String pointCutName) {

        return CommandProps.of()
                .behavior(getClassFromProps("cmdClass", pointCutName),
                        getClassFromProps("algorithmClass", pointCutName))
                .range(getPropertyForPointcut(pointCutName,
                        "highValue",Long.class),
                        getPropertyForPointcut(pointCutName,
                                "lowValue",Long.class))
                .temporal(getPropertyForPointcut(pointCutName,
                        "startTimeMs",Long.class),
                        getPropertyForPointcut(pointCutName,
                                "periodMs",Long.class),
                        getPropertyForPointcut(pointCutName,
                                "offPeriodMs",Long.class))
                .percentErrors(getPropertyForPointcut(pointCutName,
                        "percentErrors",Double.class))
                .buildRaw();
    }

    private Class getClassFromProps(String type, String pointCutName) {
        Class clazz = null;

        try {
            clazz = Class.forName(getPropertyForPointcut(pointCutName,
                    type,String.class));

        } catch (Exception e) {
            logger.warn("Cannot load the {}" +
                            ".cmdClass configuration." +
                            "Sourcing the command from the annotation" +
                            "configuration",
                    pointCutName);
        }

        return clazz;
    }

    private <T> T getPropertyForPointcut(String pointCutName,
                                         String propertyName,
                                         Class<T> returnType) {
        String prefix = pointCutName + ".nf-instrument.";

        String fqpn = prefix + propertyName;

        return environment.getProperty(fqpn,returnType);
    }
}
