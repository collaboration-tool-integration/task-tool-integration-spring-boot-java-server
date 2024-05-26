package com.pbl.tasktoolintegration.monday.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${monday.key}")
    private String apiKey;

    @Bean
    public WebClient mondayWebClient() {

        return WebClient
            .builder()
            .baseUrl("https://api.monday.com/v2")
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
            .defaultHeader("API-version", "2023-10")
            .defaultHeader("Authorization", apiKey)
            .build();
    }
}
