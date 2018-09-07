# Non-functional Behavior Instrumentation

## Overview

Have you ever wanted to demonstrate or verify fault-tolerance
characteristics of your Java or Spring application, but you cannot
without a complicated load test or poluting your code with
non-functional logic?

If so, this project is for you.

## Prerequisites

-   Applications running Java 8 or greater

-   Apache JMeter 4.0 or greater

### What it does

This project provides a framework, tools, and an example of how to
instrument non-functional behaviors within your applications, with a
minimum of fuss.

The project is built upon five key concepts:

1.  **Non-functional characteristic** - A
    *non-functional characteristic* is a system characteristic that is
    tangential to its core function or purpose.
    We normally think of non-functional characteristics as related to
    performance, although they need not be.
    Non-functional characteristics simulated by this project include
    latency and runtime errors (such as Socket or other other system
    exceptions).

1.  **Algorithm** - An algorithm is a rule or function applied to an
    input, and yields a determinate output.
    In this project, algorithms are used to provide an output value of a
    non-functional characteristic.
    There are two types in use by this project:

    -   **Temporal** - The algorithm calculates a value based off of a
        function of time.

    -   **Pseudo Random** - The algorithm calculates a value based off
        of a pseudo-random generator.

1.  **Non-functional command** - A *non-functional command* executes a
    non-functional characteristic according to an applied *algorithm*.

1.  **Instrumentation** - Application methods are instrumented to inject
    non-functional commands into its code.

1.  **Command Processor** - The command processor will select the
    appropriate non-functional command based on the instrumented method
    configuration.

### Project structure

The project contains four modules to get you up and running:

1.  `nf-instrument` contains core set of algorithms and non-functional
    commands, the instrumentation annotation and associated aspect.

1.  `nf-instrument-boot` contains default Spring Boot configuration for
    the most common commands and associated algorithms.

1.  `demo-config-server` is a simple local config server for testing out
    the project.

1.  `demo-app` is a simple demo application of how to use the
    instrumentation annotations and associated

1.  `scripts` contains set of Jmeter test scripts and sample test plan
    to drive load against the demo application.

1.  `repo` contains a sample instrumentation configuration.
    You may config this as a git repo and source it from the
    `demo-config-server`.

### How it works

The core project provides the basic instrumentation logic.

#### Core project components

1.  A
    [Non-functional *command* interface](./instrument/src/main/java/io/pivotal/pal/instrumentation/commands/BehaviorCmd.java).
    A *command* is non-functional *characteristic* that varies by
    an applied *algorithm*.

1.  A
    [Non-functional *algorithm* interface](./instrument/src/main/java/io/pivotal/pal/instrumentation/algorithms/Algorithm.java).
    An *algorithm* is applied to a *command* to vary the command's
    non-functional *characteristic*.

1.  An
    [*Injection Annotation*](./instrument/src/main/java/io/pivotal/pal/instrumentation/InjectNfBehavior.java)
    that marks an instrumentation point at a
    method within a Java application.
    It also contains static configuration for a command and its
    algorithm.

1.  An
    [*Injection aspect*](./instrument/src/main/java/io/pivotal/pal/instrumentation/aspects/InjectBehaviorAspect.java)
    that executes non-functional *commands* according to
    either static configuration (declared through an instrumented
    method's *injection annotation*), or external configuration.

    The aspect leverages the *injection annotation* as the pointcut.

1.  The aspect executes the
    [*command processor*](./instrument/src/main/java/io/pivotal/pal/instrumentation/config/factories/CommandFactory.java).
    The
    [*Injection Annotation*](./instrument/src/main/java/io/pivotal/pal/instrumentation/InjectNfBehavior.java)
    must be configured with a `pointCutName()`.
    The point cut name will be used to reference external configuration.

1.  The
    [*algorithm configuration*](./instrument/src/main/java/io/pivotal/pal/instrumentation/config/CommandProps.java)
    is a simple POJO that models command and algorithm parameters.

1.  The
    [*Injection Annotation*](./instrument/src/main/java/io/pivotal/pal/instrumentation/InjectNfBehavior.java)
    contains default configuration for the command and algorithm
    properties.
    Each may be overriden statically in the annotation, or dynamically
    through external configuration.


#### Spring boot extensions

The `instrument-boot` module bootifies the core project functionality
in the following ways:

1.  Imports the instrumentation libraries through the
    [`@EnableNfBehaviorInstrumentation` annotation](./instrument-boot/src/main/java/io/pivotal/pal/instrumentation/config/EnableNfBehaviorInstrumentation.java).

1.  Provides a dynamic command factory injected in the
    [Spring Boot configuration](./instrument-boot/src/main/java/io/pivotal/pal/instrumentation/config/BehaviorInstrumentConfig.java)
    `injectBehaviorAspect()`.

1.  An
    [example of external configuration is provided for you](./repo/application.yml).

## Build it

1.  Clone this project to your workstation.

1.  Build it from the project directory:

    ```bash
    ./gradle clean build
    ```
    The build is defaulted to Spring Boot 2.0 and Spring Cloud
    `Finchley`.

    If you prefer to build on a different version of Spring Boot and/or
    Spring Cloud, you may override the
    [Gradle properties](./gradle.properties).
    Just make sure you override all the dependency versions to align
    on your Spring Cloud release train, Spring Boot, and its associated
    `aspectJ` and `junit` dependencies.
    If you plan to privately publish, make sure to align
    your `buildVersion` accordingly.

1.  Optionally install the published artifacts to your local M2 repo:

    ```bash
    ./gradle publishToMavenLocal
    ```

1.  The relevant jars you will need to consume in your applications are:

    -   `nf-instrument` is the core instrumentation logic, annotation
        and aspect that wires non-functional behavior to your code.

    -   `nf-instrument-boot` is the Spring Boot configuration adapter
        for the core instrumentation logic, and contains default
        configurations for each algorithm type.

1.  If you will reference as a maven dependency, include the following
    in your build scripts:

    -   Maven POM:

        ```xml
        <dependencies>
            ...
            <dependency>
                <groupId>io.pivotal.pal.instrumentation</groupId>
                <artifactId>nf-instrument</artifactId>
                <version>${version}</version>
            </dependency>
            <dependency>
                <groupId>io.pivotal.pal.instrumentation</groupId>
                <artifactId>nf-instrument-boot</artifactId>
                <version>${version}</version>
            </dependency>
            ...
        <dependencies>
        ```

    -   Gradle:

        ```groovy
        dependencies {
            ...
            compile('io.pivotal.pal.instrumentation:nf-instrument:$version')
            compile('io.pivotal.pal.instrumentation:nf-instrument-boot:$version')
            ...
        }
        ```

1.  If you manually configure, run the associated dependencies plugin
    to get a list of direct and transistive dependencies to configure
    in your project:

    ```bash
    ./gradlew :instrument:dependencies --configuration compile
    ./gradlew :instrument-boot:dependencies --configuration compile
    ```

## Configure it

Configuring your Spring Boot application to use non-functional
instrumentation is done via three basic steps:

1.  Add dependencies to your app.
1.  Annotate your app and associated methods to instrument with specific
    algorithms and settings.
1.  Configure any dynamic algorithm settings through external
    configuration.

For near-instant gratification, review the `demo-app`, and its
associated artifacts:

- [Spring Boot application](./demo-app/src/main/java/io/pivotal/pal/instrumentation/demo/DemoInstrumentedAppApplication.java)
- [Instrumented Controller](./demo-app/src/main/java/io/pivotal/pal/instrumentation/demo/DemoController.java)
- [External configuration](./repo/application.yml)

You will still need to [configure its dependencies](#dependencies).

It is also important to understand the
[command and algorithm configuration](#commands-algorithms-and-rules).
There are rigid rules each command and associated algorithm
configuration must comply, otherwise your app will fail during startup,
or after dynamic refresh.

### Dependencies

Add the following dependencies:

#### Gradle

```groovy
compile("io.pivotal.pal.instrumentation:nf-instrument:${version}")
compile("io.pivotal.pal.instrumentation:nf-instrument-boot:${version}")
```

#### Maven

```xml
<dependencies>
    <dependency>
        <groupId>io.pivotal.pal.instrumentation</groupId>
        <artifactId>nf-instrument</artifactId>
        <version>${version}</version>
    </dependency>
    <dependency>
        <groupId>io.pivotal.pal.instrumentation</groupId>
        <artifactId>nf-instrument-boot</artifactId>
        <version>${version}</version>
    </dependency>
</dependencies>
```

### Enable instrumentation in your Spring Boot app

Annotate your `SpringBootApplication` class with
[`@EnableNfBehaviorInstrumentation` annotation](./instrument-boot/src/main/java/io/pivotal/pal/instrumentation/config/EnableNfBehaviorInstrumentation.java).

### Enable methods for instrumentation

Annotate your method to be injected with non-functional commands with
[`@InjectNfBehavior`](./instrument/src/main/java/io/pivotal/pal/instrumentation/InjectNfBehavior.java)

### Enable methods for static or dynamic instrumentation

1.  Specify the
    [`@InjectNfBehavior`](./instrument/src/main/java/io/pivotal/pal/instrumentation/InjectNfBehavior.java)
    annotation `pointCutName()`.
    It must be unique across the application.

1.  You may statically declare the command and algorithm parameters in
    the annotation, if it better serves your use case.

1.  Configure the [external configuration](./repo/application.yml).
    If you configure both the static annotation configuration, the
    external configuration wins.

If you do not configure either statically in the annotation in external
configuration, it will source the defaults from the annotation.

## Run it

## Commands, algorithms and rules

### Cross-cutting configuration

The current solution takes approach of cross cutting a single simple
configuration template.

#### Range values

These control high and low watermark values returned by a given
algorithm:

- High Value
- Low Value

For example, in latency based commands, you will typically want to
bound the upper and lower limits, especially when applying temporal or
pseudo-random algorithms.

#### Temporal values

- **Start Time** (Referenced from Unix Epoch in Milliseconds)
- **Period** (Milliseconds)
- **Off Period** (Milliseconds)

These are required to set for temporal type algorithms.
The *Off Period* value is used by pulse, notch or square algorithms to
set the duration of the period where the value is the *Low Value*.

The *Start Time* is set to reference where in the temporary cycle to
start.
This is useful for long period cycles.

#### Error threshold

-   **Error Percentage** - is the percentage of runtime errors to throw,
    based off a pseudo-random generator.

### Temporal algorithms

The solution currently provides four types of temporal algorithms:

1.  **Steady State** - The algorithm returns the configured *High Value*
    regardless of period configuration.

1.  **Pulse** - The algorthim returns the configured *Low Value* for the
    *Off Period* duration within a period, then transitions to the
    configured *High Value* for the remainder of the period.

    You can provide a *Steady State* behavior by configuring one of the
    following:
    - *Low Value* is set to the *High Value*
    - *Off Period* to zero.

    The pulse rule is also imbedded within Ramp and Sine algorithms if
    you want to combine them in a single configuration.

1.  **Sine** - The algorithm returns a value ranging between configured
    *High Value* and *Low Value* according to Sine function applied to
    the time within the period referenced from the *Start Time*.

1.  **Ramp** - The algorithm returns a value ranging between configured
    *Low Value* and *High Value* according to linear ramp function
    applied to the time within the period referenced from the
    *Start Time*.

### Pseudo-random algorithm

The current solution uses Pseudo-random algorithm to throw runtime
exceptions.

### Configuration Rules

There are three types of rules to remember when constructing a
configuration:

-   **Range Rules** - the *High Value* must be greater than or equal to
    the *Low Value*.
-   **Temporal Rules** - the *Off Period* must be less than or equal to
    the *Period*.
-   **Percent Rules** - the *Error Percentage* must be between 0 and 1
    inclusive.

None of the configuration parameters may be less than zero.

## Troubleshooting and known issues

### Your application fails to start

-   You improperly configured your command algorithm.
    The configuration rules are violated.

-   Verify the [configuration rules](#configuration-rules)

### Aspect does not execute non-functional commands

You either did not:

-   Annotate your application to enable instrumentation.

-   Properly configure your injection annotations.

## Configuration appendix

## Roadmap

### Command Processor

- Expose means to provide pointcut patterns externally.

### Commands

- Pseudo-random latency commands
- CPU spin
- Memory use
- OOM
- Stuck thread