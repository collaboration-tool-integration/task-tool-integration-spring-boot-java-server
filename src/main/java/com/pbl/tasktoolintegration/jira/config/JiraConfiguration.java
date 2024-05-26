package com.pbl.tasktoolintegration.jira.config;

import com.atlassian.adf.jackson2.AdfJackson2;
import com.atlassian.adf.model.node.Doc;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Configuration
@Slf4j
public class JiraConfiguration {
    @Value("${jira.site-url}")
    private String jiraSiteUrl;

    @Value("${jira.auth-email}")
    private String jiraAuthEmail;

    @Value("${jira.api-token}")
    private String jiraApiToken;

    @Bean(name = "jiraObjectMapper")
    public ObjectMapper objectMapper() {
        SimpleModule simpleModule = new SimpleModule()
                .addDeserializer(Doc.class, new ADFDeserializer());
        return adfJackson2().mapper()
                .registerModule(new JavaTimeModule())
                .registerModule(simpleModule)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Bean
    public AdfJackson2 adfJackson2() {
        return new AdfJackson2();
    }

    @Bean
    public WebClient jiraWebClient() {
        ObjectMapper objectMapper = this.objectMapper();
        ExchangeStrategies exchangeStrategies = ExchangeStrategies
                .builder()
                .codecs(clientCodecConfigurer -> {
                    clientCodecConfigurer.defaultCodecs().maxInMemorySize(-1);
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
//                .filter(ExchangeFilterFunction.ofResponseProcessor(
//                        clientResponse -> Mono.just(clientResponse.mutate()
//                                .body(dataBufferFlux -> dataBufferFlux.map(dataBuffer -> {
//                                    log.info(dataBuffer.toString(StandardCharsets.UTF_8));
//                                    return dataBuffer;
//                                }))
//                                .build())
//                ))
                .build();
    }
}
