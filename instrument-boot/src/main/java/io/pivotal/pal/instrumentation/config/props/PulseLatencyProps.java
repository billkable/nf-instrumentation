package io.pivotal.pal.instrumentation.config.props;

import io.pivotal.pal.instrumentation.config.AlgorithmProps;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "pulse-latency.command")
public class PulseLatencyProps
        extends AlgorithmProps {}
