package com.pbl.tasktoolintegration.jira.common.config.converter;

import com.pbl.tasktoolintegration.jira.model.request.ResponseTimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class ResponseTimeUnitConverter implements Converter<String, ResponseTimeUnit> {
    @Override
    public ResponseTimeUnit convert(String source) {
        try {
            return ResponseTimeUnit.valueOf(source.toUpperCase());
        }
        catch (Exception e) {
            log.warn("ResponseTimeUnit Converter: Not Matched " + source);
            throw new RuntimeException("Matched Unit Not Found");
        }
    }
}
