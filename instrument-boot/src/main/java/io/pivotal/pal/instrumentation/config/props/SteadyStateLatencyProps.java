package io.pivotal.pal.instrumentation.config.props;

import io.pivotal.pal.instrumentation.config.AlgorithmProps;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ss-latency.command")
public class SteadyStateLatencyProps
        extends AlgorithmProps {}
