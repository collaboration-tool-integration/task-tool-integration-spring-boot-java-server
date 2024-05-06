package com.pbl.tasktoolintegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TaskToolIntegrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskToolIntegrationApplication.class, args);
    }

}
