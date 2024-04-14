package com.pbl.tasktoolintegration.jira.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class JiraPaging {
    @JsonProperty("maxResults")
    private int maxResult;

    @JsonProperty("startAt")
    private int startAt;

    @JsonProperty("total")
    private int total;
}
