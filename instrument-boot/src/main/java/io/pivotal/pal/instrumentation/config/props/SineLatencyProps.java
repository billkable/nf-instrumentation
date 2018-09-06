package io.pivotal.pal.instrumentation.config.props;

import io.pivotal.pal.instrumentation.config.AlgorithmProps;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sine-latency.command")
public class SineLatencyProps
        extends AlgorithmProps {}