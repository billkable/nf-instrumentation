package io.pivotal.pal.instrumentation.config;

import io.pivotal.pal.instrumentation.commands.BehaviorCmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.cloud.bus.ServiceMatcher;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

import java.util.HashMap;
import java.util.Map;

@Endpoint(id = "nf-instrument")
@RemoteApplicationEventScan
public class BehaviorCmdEndpoint {
    private final Logger logger = LoggerFactory
            .getLogger(BehaviorCmdEndpoint.class);
    private final CommandFactory commandFactory;
    private final ServiceMatcher busServiceMatcher;
    private final ApplicationEventPublisher publisher;

    public BehaviorCmdEndpoint(CommandFactory commandFactory,
                               ServiceMatcher busServiceMatcher,
                               ApplicationEventPublisher publisher) {
        this.commandFactory = commandFactory;
        this.busServiceMatcher = busServiceMatcher;
        this.publisher = publisher;
    }

    @ReadOperation
    public Map<String, CommandPropsDto> getCache() {
        logger.debug("Getting BehaviorCmd cache");

        Map<String, CommandPropsDto> dtoMap = new HashMap<>();

        for (Map.Entry<String, BehaviorCmd>entry:
                commandFactory.getCommands().entrySet()) {
            dtoMap.put(entry.getKey(),
                    CommandPropsDto.fromCmd(
                            entry.getKey(),
                            entry.getValue()));
        }

        return dtoMap;
    }

    @WriteOperation
    public void putCommand(String pointCutName,
                           String cmdClass,
                           String algorithmClass,
                           Long highValue,
                           Long lowValue,
                           Long startTimeMs,
                           Long periodMs,
                           Long offPeriodMs,
                           Double percentErrors
    ) {

        CommandPropsDto propsDto =
                new CommandPropsDto();

        propsDto.pointCutName = pointCutName;
        propsDto.cmdClass = cmdClass;
        propsDto.algorithmClass = algorithmClass;
        propsDto.highValue = highValue;
        propsDto.lowValue = lowValue;
        propsDto.startTimeMs = startTimeMs;
        propsDto.periodMs = periodMs;
        propsDto.offPeriodMs = offPeriodMs;
        propsDto.percentErrors = percentErrors;

        this.publisher.publishEvent(new BehaviorCmdChangeEvent(
                        this, busServiceMatcher.getServiceId(),
                                propsDto));
    }

    private void putCmdToCache(CommandPropsDto propsDto) {
        logger.debug("Putting a BehaviorCmd into cache");
        this.commandFactory.putCmd(propsDto.getPointCutName(),
                propsDto.toProps());
    }

    @EventListener
    public void onBehaviorCmdChangeEvent(BehaviorCmdChangeEvent event) {

        CommandPropsDto propsDto = event.getPropsDto();

        putCmdToCache(propsDto);
    }

    public static class CommandPropsDto {
        private String pointCutName;
        private String cmdClass;
        private String algorithmClass;
        private Long highValue;
        private Long lowValue;
        private Long startTimeMs;
        private Long periodMs;
        private Long offPeriodMs;
        private Double percentErrors;

        public CommandPropsDto() {}

        public CommandPropsDto(
                String pointCutName,
                String cmdClass,
                String algorithmClass,
                Long highValue,
                Long lowValue,
                Long startTimeMs,
                Long periodMs,
                Long offPeriodMs,
                Double percentErrors
        ) {
            this.pointCutName = pointCutName;
            this.cmdClass = cmdClass;
            this.algorithmClass = algorithmClass;
            this.highValue = highValue;
            this.lowValue = lowValue;
            this.startTimeMs = startTimeMs;
            this.periodMs = periodMs;
            this.offPeriodMs = offPeriodMs;
            this.percentErrors = percentErrors;
        }

        public String getPointCutName() {
            return pointCutName;
        }

        public void setPointCutName(String pointCutName) {
            this.pointCutName = pointCutName;
        }

        public String getCmdClass() {
            return cmdClass;
        }

        public void setCmdClass(String cmdClass) {
            this.cmdClass = cmdClass;
        }

        public String getAlgorithmClass() {
            return algorithmClass;
        }

        public void setAlgorithmClass(String algorithmClass) {
            this.algorithmClass = algorithmClass;
        }

        public Long getHighValue() {
            return highValue;
        }

        public void setHighValue(Long highValue) {
            this.highValue = highValue;
        }

        public Long getLowValue() {
            return lowValue;
        }

        public void setLowValue(Long lowValue) {
            this.lowValue = lowValue;
        }

        public Long getStartTimeMs() {
            return startTimeMs;
        }

        public void setStartTimeMs(Long startTimeMs) {
            this.startTimeMs = startTimeMs;
        }

        public Long getPeriodMs() {
            return periodMs;
        }

        public void setPeriodMs(Long periodMs) {
            this.periodMs = periodMs;
        }

        public Long getOffPeriodMs() {
            return offPeriodMs;
        }

        public void setOffPeriodMs(Long offPeriodMs) {
            this.offPeriodMs = offPeriodMs;
        }

        public Double getPercentErrors() {
            return percentErrors;
        }

        public void setPercentErrors(Double percentErrors) {
            this.percentErrors = percentErrors;
        }

        public CommandProps toProps() {
            return CommandProps.of()
                    .behavior(cmdClass,algorithmClass)
                    .range(highValue,lowValue)
                    .temporal(startTimeMs,
                            periodMs,offPeriodMs)
                    .percentErrors(percentErrors)
                    .build();
        }

        public static CommandPropsDto fromMap(Map map) {
            return new CommandPropsDto(
                    (String)map.get("pointCutName"),
                    (String)map.get("cmdClass"),
                    (String)map.get("algorithmClass"),
                    (Long)map.get("highValue"),
                    (Long)map.get("lowValue"),
                    (Long)map.get("startTimeMs"),
                    (Long)map.get("periodMs"),
                    (Long)map.get("offPeriodMs"),
                    (Double)map.get("percentErrors"));
        }

        public static CommandPropsDto fromCmd(String pointCutName,
                                                                          BehaviorCmd cmd) {
            return new CommandPropsDto(
                    pointCutName,
                    cmd.getClass().getName(),
                    cmd.getAlgorithm().getClass().getName(),
                    cmd.getAlgorithm().getProps().getHighValue(),
                    cmd.getAlgorithm().getProps().getLowValue(),
                    cmd.getAlgorithm().getProps().getStartTimeMs(),
                    cmd.getAlgorithm().getProps().getPeriodMs(),
                    cmd.getAlgorithm().getProps().getOffPeriodMs(),
                    cmd.getAlgorithm().getProps().getPercentErrors()
            );
        }
    }

    public static class BehaviorCmdChangeEvent extends RemoteApplicationEvent {

        private CommandPropsDto propsDto;

        protected BehaviorCmdChangeEvent() {}

        public BehaviorCmdChangeEvent(Object source,
                                      String originService,
                                      CommandPropsDto propsDto) {
            super(source, originService);
            this.propsDto = propsDto;
        }

        public CommandPropsDto getPropsDto() {
            return propsDto;
        }

        public void setPropsDto(CommandPropsDto propsDto) {
            this.propsDto = propsDto;
        }
    }

}
