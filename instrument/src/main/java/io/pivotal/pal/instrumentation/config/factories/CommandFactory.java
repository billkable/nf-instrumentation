package io.pivotal.pal.instrumentation.config.factories;

import io.pivotal.pal.instrumentation.commands.BehaviorCmd;

public interface CommandFactory {
    BehaviorCmd getCommand(Object key);
}
