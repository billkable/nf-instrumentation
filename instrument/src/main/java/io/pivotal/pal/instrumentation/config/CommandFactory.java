package io.pivotal.pal.instrumentation.config;

import io.pivotal.pal.instrumentation.InjectNfBehavior;
import io.pivotal.pal.instrumentation.algorithms.Algorithm;
import io.pivotal.pal.instrumentation.commands.BehaviorCmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private final Logger logger = LoggerFactory.getLogger(CommandFactory.class);
    private final Map<String,BehaviorCmd> cacheManager = new HashMap<>();
    private final CommandPropsSource externalCommandPropsSource;

    public CommandFactory(CommandPropsSource externalCommandPropsSource) {
        this.externalCommandPropsSource = externalCommandPropsSource;
    }

    public BehaviorCmd getCommand(Object key) {

        InjectNfBehavior annotation =
                (InjectNfBehavior)key;

        BehaviorCmd cmd = this.cacheManager.get(annotation.pointCutName());

        if (cmd == null) {
            logger.info("Command not found in cache for point cut {}",
                    annotation.pointCutName());

            CommandProps props =
                    getAnnotationCommandProps(annotation);

            props = overrideAnnotationCommandProps(
                    annotation.pointCutName(),
                    props);

            cmd = generateCommand(props);

            cacheManager.put(annotation.pointCutName(), cmd);

            logger.info("Added command {} to cache for point cut {}",
                    cmd.getClass().getName(),
                    annotation.pointCutName());

        }

        return cmd;
    }

    private BehaviorCmd generateCommand(CommandProps props) {
        Class<BehaviorCmd> behaviorCmdClass
                = props.getCmdClass();

        Constructor<BehaviorCmd> constructedBehaviorCmd;

        Class<Algorithm> algorithmClass
                = props.getAlgorithmClass();

        Constructor<Algorithm> constructedTemporalAlgorithm;

        try {
            constructedTemporalAlgorithm
                    = algorithmClass.getConstructor(CommandProps.class);

            Algorithm algorithm = constructedTemporalAlgorithm.newInstance(props);

            constructedBehaviorCmd
                    = behaviorCmdClass.getConstructor(Algorithm.class);

            return constructedBehaviorCmd.newInstance(algorithm);

        } catch (Exception e) {
            throw new IllegalStateException(
                    "InjectNfBehavior " +
                            "annotation is not configured correctly");
        }
    }

    private CommandProps overrideAnnotationCommandProps(
            String pointCutName,
            CommandProps annotationProps) {

        CommandProps externalProps
                = externalCommandPropsSource.getProps(pointCutName);

        return CommandProps.override(annotationProps,externalProps);
    }

    private CommandProps getAnnotationCommandProps(InjectNfBehavior annotation) {

        return CommandProps.of()
                .behavior(annotation.cmdClass(),
                        annotation.algorithmClass())
                .range(annotation.highValue(),
                        annotation.lowValue())
                .temporal(annotation.startTimeMs(),
                        annotation.periodMs(),
                        annotation.offPeriodMs())
                .percentErrors(annotation.percentErrors())
                .build();

    }

    public void putCmd(String pointCutName, CommandProps props) {
        this.cacheManager.put(pointCutName,
                generateCommand(props));
    }

    public Map<String,BehaviorCmd> getCommands() {
        return this.cacheManager;
    }
}
