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
        String prefix = pointCutName + ".nf-instrument.";

        Class cmdClass = null;

        try {
            cmdClass = Class.forName(getPropertyForPrefix(prefix,
                    "cmdClass",String.class));

        } catch (Exception e) {
            logger.warn("Cannot load the {}" +
                            ".cmdClass configuration." +
                            "Sourcing the command from the annotation" +
                            "configuration",
                            pointCutName);
        }

        Class algorithmClass = null;

        try {
            algorithmClass = Class.forName(getPropertyForPrefix(prefix,
                        "algorithmClass",String.class));

        } catch (Exception e) {
            logger.warn("Cannot load the {}" +
                            ".algorithmClass configuration." +
                            "Sourcing the command from the annotation" +
                            "configuration",
                    pointCutName);
        }

        // TODO algorithm props builder

        CommandProps props = new CommandProps(
                cmdClass,

                algorithmClass,

                getPropertyForPrefix(prefix,
                "highValue",Long.class),

                getPropertyForPrefix(prefix,
                        "lowValue",Long.class),

                getPropertyForPrefix(prefix,
                        "startTimeMs",Long.class),

                getPropertyForPrefix(prefix,
                        "periodMs",Long.class),

                getPropertyForPrefix(prefix,
                        "offPeriodMs",Long.class),

                getPropertyForPrefix(prefix,
                        "percentErrors",Double.class)

        );

        return props;
    }

    private <T> T getPropertyForPrefix(String prefix,
                                        String propertyName,
                                        Class<T> returnType) {

        String fqpn = prefix + propertyName;

        return environment.getProperty(fqpn,returnType);
    }
}
