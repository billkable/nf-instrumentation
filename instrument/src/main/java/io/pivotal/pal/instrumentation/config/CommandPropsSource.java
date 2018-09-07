package io.pivotal.pal.instrumentation.config;

public interface CommandPropsSource {
    CommandProps getProps(String pointCutName);
}
