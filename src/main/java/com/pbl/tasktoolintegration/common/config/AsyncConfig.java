package com.pbl.tasktoolintegration.common.config;

import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;

@Configuration
public class AsyncConfig {
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutorBuilder()
                .corePoolSize(3)
                .maxPoolSize(10)
                .queueCapacity(100)
                .threadNamePrefix("plb-async-thread-")
                .awaitTerminationPeriod(Duration.ofSeconds(60))
                .build();
        taskExecutor.initialize();

        return taskExecutor;
    }
}
