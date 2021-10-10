package com.sas;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
@Slf4j
@EnableScheduling
public class SchedulingConfig implements SchedulingConfigurer {

    @Autowired
    private Producer producer;

    @Value("${app.config.timeout.min}")
    private int timeoutMin;

    @Value("${app.config.timeout.max}")
    private int timeoutMax;

    @Bean
    public Executor taskExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
        taskRegistrar.addTriggerTask(
                () -> producer.sendMessage(),
                context -> {
                    Optional<Date> lastCompletionTime = Optional.ofNullable(context.lastCompletionTime());
                    int randomTimeOut = generateTimeOut();
                    log.info("Next delivery will be after {} seconds ", randomTimeOut);
                    Instant nextExecutionTime =  lastCompletionTime.orElseGet(Date::new).toInstant().plusSeconds(randomTimeOut);
                    return Date.from(nextExecutionTime);
                }
        );
    }

    private int generateTimeOut() {
        return ThreadLocalRandom.current().nextInt(timeoutMin, timeoutMax);
    }

}