package io.pivotal.pal.instrumentation.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "behavior.command")
public class BehaviorCmdHealthProp {
    private boolean healthOk = true;

    public boolean isHealthOk() {
        return healthOk;
    }

    public void setHealthOk(boolean healthOk) {
        this.healthOk = healthOk;
    }
}
