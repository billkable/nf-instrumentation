package io.pivotal.pal.instrumentation.config;

import io.pivotal.pal.instrumentation.InjectBehaviorAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BehaviorInstrumentConfig {
    private final Logger logger = LoggerFactory.getLogger(BehaviorInstrumentConfig.class);
    private final ApplicationContext ctx;

    public BehaviorInstrumentConfig(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Bean
    @RefreshScope
    public CommandFactory commandFactory() {
        logger.info("Generating or refreshing non-functional " +
                "instrumentation command factory and associated caches");
        return new CommandFactory(
                new CommandPropsEnvironmentSource(ctx.getEnvironment()));
    }

    @Bean
    public InjectBehaviorAspect injectBehaviorAspect() {
        logger.info("Generating non-functional instrumentation aspect");
        return new InjectBehaviorAspect(
            commandFactory()
        );
    }
}
