package com.pbl.tasktoolintegration.jira.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JiraDeadlineDto {
    @JsonProperty("userName")
    private String userName;

    @JsonProperty("deadlineExceededIssueCount")
    private int deadlineExceededIssueCount;
}
