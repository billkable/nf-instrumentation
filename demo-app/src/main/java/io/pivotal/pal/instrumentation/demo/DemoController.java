package io.pivotal.pal.instrumentation.demo;

import io.pivotal.pal.instrumentation.InjectNfBehavior;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class DemoController {

    @InjectNfBehavior(pointCutName = "hello")
    @GetMapping
    public String hello() {
        return "Hello";
    }


    @InjectNfBehavior(pointCutName = "static-hello",
                                highValue = 500L)
    @GetMapping("/static")
    public String staticHello() {
        return "Static Hello";
    }
}