package io.pivotal.pal.instrumentation.config.factories;

import io.pivotal.pal.instrumentation.InjectNfBehavior;
import io.pivotal.pal.instrumentation.algorithms.Algorithm;
import io.pivotal.pal.instrumentation.commands.BehaviorCmd;
import io.pivotal.pal.instrumentation.config.AlgorithmProps;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class StaticCommandFactory implements CommandFactory {
    private CommandFactory dynamicProcessor;
    private final Map<String, BehaviorCmd> cmdMap = new HashMap<>();

    public StaticCommandFactory(CommandFactory dynamicProcessor) {
        this.dynamicProcessor = dynamicProcessor;
    }

    public BehaviorCmd getCommand(Object key) {

        InjectNfBehavior annotation
            = (InjectNfBehavior)key;

        BehaviorCmd cmd;

        if (!annotation.name().equals("")) {

            cmd = cmdMap.get("name");

            if (cmd == null) {

                AlgorithmProps props =
                        getConfiguredCyclicProps(annotation);

                Class<BehaviorCmd> behaviorCmdClass
                        = annotation.cmdClass();

                Constructor<BehaviorCmd> constructedBehaviorCmd;

                Class<Algorithm> algorithmClass
                        = annotation.algorithmClass();


                Constructor<Algorithm> constructedTemporalAlgorithm;
                try {
                    constructedTemporalAlgorithm
                            = algorithmClass.getConstructor(AlgorithmProps.class);

                    Algorithm algorithm = constructedTemporalAlgorithm.newInstance(props);

                    constructedBehaviorCmd
                            = behaviorCmdClass.getConstructor(Algorithm.class);

                    cmd = constructedBehaviorCmd.newInstance(algorithm);
                } catch (Exception e) {
                    throw new IllegalStateException(
                            "InjectNfBehavior " +
                                    "annotation is not configured correctly");
                }

                cmdMap.put(annotation.name(), cmd);
            }
        } else if (!annotation.cmdKey().equals("")) {
            cmd = dynamicProcessor.getCommand(annotation.cmdKey());
        } else
            throw new IllegalStateException(
                    "InjectNfBehavior " +
                            "annotation is not configured correctly");

        return cmd;
    }

    private AlgorithmProps getConfiguredCyclicProps(InjectNfBehavior annotation) {
        AlgorithmProps props = new AlgorithmProps();

        props.setHighValue(annotation.highValue());
        props.setLowValue(annotation.lowValue());
        props.setPeriodMs(annotation.periodMs());
        props.setStartTimeMs(annotation.startTimeMs());
        props.setOffPeriodMs(annotation.offPeriodMs());
        props.setPercentErrors(annotation.percentErrors());

        return props;
    }
}
