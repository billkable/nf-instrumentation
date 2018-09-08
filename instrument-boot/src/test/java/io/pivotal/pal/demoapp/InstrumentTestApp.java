package io.pivotal.pal.demoapp;

import io.pivotal.pal.instrumentation.config.EnableNfBehaviorInstrumentation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableNfBehaviorInstrumentation
public class InstrumentTestApp {
    public static void main(String[] args) {
        SpringApplication.run(InstrumentTestApp.class, args);
    }
}
