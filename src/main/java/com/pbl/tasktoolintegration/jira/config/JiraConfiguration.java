package com.pbl.tasktoolintegration.jira.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class JiraConfiguration {
    @Value("${jira.site-url")
    private String jiraSiteUrl;

    @Value("${jira.auth_email}")
    private String jiraAuthEmail;

    @Value("${jira.api_token}")
    private String jiraApiToken;

    @Bean
    public WebClient jiraWebClient() {
        return WebClient.builder()
                .baseUrl(jiraSiteUrl)
                .defaultHeaders(
                        httpHeaders -> httpHeaders.setBasicAuth(jiraAuthEmail, jiraApiToken)
                )
                .build();
    }
}
