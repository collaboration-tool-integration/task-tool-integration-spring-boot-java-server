package com.pbl.tasktoolintegration.jira.config;

import com.atlassian.adf.jackson2.AdfJackson2;
import com.atlassian.adf.model.node.Doc;
import com.atlassian.adf.model.node.Node;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.netty.handler.logging.LogLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
public class JiraConfiguration {
    @Value("${jira.site-url}")
    private String jiraSiteUrl;

    @Value("${jira.auth-email}")
    private String jiraAuthEmail;

    @Value("${jira.api-token}")
    private String jiraApiToken;

    @Bean
    public WebClient jiraWebClient() {
        SimpleModule simpleModule = new SimpleModule()
                .addDeserializer(Doc.class, new ADFDeserializer());
        ObjectMapper objectMapper = new AdfJackson2().mapper()
                .registerModule(new JavaTimeModule())
                .registerModule(simpleModule)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ExchangeStrategies exchangeStrategies = ExchangeStrategies
                .builder()
                .codecs(clientCodecConfigurer -> {
                    clientCodecConfigurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
                    clientCodecConfigurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
                })
                .build();

        return WebClient.builder()
                .baseUrl(jiraSiteUrl + "/rest/api/3")
                .exchangeStrategies(exchangeStrategies)
                .defaultHeaders(
                        httpHeaders -> {
                            httpHeaders.setAccept(List.of(new MediaType[]{MediaType.APPLICATION_JSON}));
                            httpHeaders.setBasicAuth(jiraAuthEmail, jiraApiToken);
                        }
                )
                .filter(ExchangeFilterFunction.ofResponseProcessor(
                        clientResponse -> Mono.just(clientResponse.mutate()
                                .body(dataBufferFlux -> dataBufferFlux.map(dataBuffer -> {
                                    log.info(dataBuffer.toString(StandardCharsets.UTF_8));
                                    return dataBuffer;
                                }))
                                .build())
                ))
                .build();
    }
}
