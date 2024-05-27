package com.pbl.tasktoolintegration.jira.common.config;

import com.pbl.tasktoolintegration.jira.common.config.converter.ResponseTimeUnitConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ResponseTimeUnitConverter());
    }
}
