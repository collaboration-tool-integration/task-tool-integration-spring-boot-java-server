package com.pbl.tasktoolintegration.jira.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JiraResponseTimeDto {
    @JsonProperty("userName")
    private String userName;

    @JsonProperty("responseTime")
    private Double responseTime;
}
