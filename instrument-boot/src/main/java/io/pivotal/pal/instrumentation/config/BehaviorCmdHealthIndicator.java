package io.pivotal.pal.instrumentation.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(BehaviorCmdHealthProp.class)
public class BehaviorCmdHealthIndicator implements HealthIndicator {
    private Logger logger = LoggerFactory
            .getLogger(BehaviorCmdHealthIndicator.class);

    private BehaviorCmdHealthProp prop;

    public BehaviorCmdHealthIndicator(BehaviorCmdHealthProp prop) {
        this.prop = prop;
    }

    @Override
    public Health health() {
        logger.debug("returning health check for BehaviorCmd");

        return prop.isHealthOk() ?
                Health.up().build():
                Health.down().build();
    }
}
