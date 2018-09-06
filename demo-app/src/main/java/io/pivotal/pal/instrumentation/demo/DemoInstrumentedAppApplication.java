package io.pivotal.pal.instrumentation.demo;

import io.pivotal.pal.instrumentation.config.EnableNfBehaviorInstrumentation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableNfBehaviorInstrumentation
public class DemoInstrumentedAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoInstrumentedAppApplication.class, args);
    }
}
