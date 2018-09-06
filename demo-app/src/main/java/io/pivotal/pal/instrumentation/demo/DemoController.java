package io.pivotal.pal.instrumentation.demo;

import io.pivotal.pal.instrumentation.InjectNfBehavior;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class DemoController {

    @InjectNfBehavior(cmdKey = "pulse-latency")
    @GetMapping
    public String hello() {
        return "Hello";
    }


    @InjectNfBehavior(name = "static-pulse-latency",
                                highValue = 1000L)
    @GetMapping("/static")
    public String staticHello() {
        return "Static Hello";
    }
}