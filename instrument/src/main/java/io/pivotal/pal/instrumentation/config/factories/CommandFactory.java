package io.pivotal.pal.instrumentation.config.factories;

import io.pivotal.pal.instrumentation.InjectNfBehavior;
import io.pivotal.pal.instrumentation.algorithms.Algorithm;
import io.pivotal.pal.instrumentation.commands.BehaviorCmd;
import io.pivotal.pal.instrumentation.config.CommandProps;
import io.pivotal.pal.instrumentation.config.CommandPropsSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private final Logger logger = LoggerFactory.getLogger(CommandFactory.class);
    private final Map<String, BehaviorCmd> cmdMap = new HashMap<>();
    private final CommandPropsSource externalCommandPropsSource;

    public CommandFactory(CommandPropsSource externalCommandPropsSource) {
        this.externalCommandPropsSource = externalCommandPropsSource;
    }

    public BehaviorCmd getCommand(Object key) {

        InjectNfBehavior annotation =
                (InjectNfBehavior)key;

        BehaviorCmd cmd = cmdMap.get(annotation.pointCutName());

        if (cmd == null) {
            logger.info("Command not found in cache for point cut {}",
                    annotation.pointCutName());

            CommandProps props =
                    getAnnotationCommandProps(annotation);

            props = overrideAnnotationCommandProps(
                    annotation.pointCutName(),
                    props);

            cmd = generateCommand(props);

            cmdMap.put(annotation.pointCutName(), cmd);

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

        return new CommandProps(
            annotation.cmdClass(),
            annotation.algorithmClass(),
            annotation.highValue(),
            annotation.lowValue(),
            annotation.startTimeMs(),
            annotation.periodMs(),
            annotation.offPeriodMs(),
            annotation.percentErrors()
        );

    }
}
