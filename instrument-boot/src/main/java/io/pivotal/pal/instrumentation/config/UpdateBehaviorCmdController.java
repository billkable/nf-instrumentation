package io.pivotal.pal.instrumentation.config;

import io.pivotal.pal.instrumentation.commands.BehaviorCmd;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Documented;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/actuator")
public class UpdateBehaviorCmdController {
    private final CommandFactory commandFactory;

    public UpdateBehaviorCmdController(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    @GetMapping("/nf-commands")
    public Map<String, CommandPropsDto> getCache() {

        Map<String, CommandPropsDto> dtoMap = new HashMap<>();

        for (Map.Entry<String, BehaviorCmd>entry:
            commandFactory.getCommands().entrySet()) {
            dtoMap.put(entry.getKey(),
                    CommandPropsDto.fromCmd(
                            entry.getValue()));
        }

        return dtoMap;
    }

    @PostMapping("/nf-command/{pointCutName}")
    public void putCommand(@PathVariable String pointCutName,
                           @RequestBody CommandPropsDto propsDto) {

        this.commandFactory.putCmd(pointCutName,propsDto.toProps());
    }

    public static class CommandPropsDto {
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
                String cmdClass,
                String algorithmClass,
                Long highValue,
                Long lowValue,
                Long startTimeMs,
                Long periodMs,
                Long offPeriodMs,
                Double percentErrors
        ) {
            this.cmdClass = cmdClass;
            this.algorithmClass = algorithmClass;
            this.highValue = highValue;
            this.lowValue = lowValue;
            this.startTimeMs = startTimeMs;
            this.periodMs = periodMs;
            this.offPeriodMs = offPeriodMs;
            this.percentErrors = percentErrors;
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

        public static CommandPropsDto fromCmd(BehaviorCmd cmd) {
            return new CommandPropsDto(
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
}
