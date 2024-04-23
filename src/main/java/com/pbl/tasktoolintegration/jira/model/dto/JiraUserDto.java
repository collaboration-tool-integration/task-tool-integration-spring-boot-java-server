package com.pbl.tasktoolintegration.jira.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pbl.tasktoolintegration.jira.entity.JiraIssueResponse;
import com.pbl.tasktoolintegration.jira.entity.JiraUser;
import lombok.Builder;
import lombok.Getter;

public class JiraUserDto {
    @Builder
    @Getter
    public static class GetJiraUserStatusDto {
        @JsonProperty("id")
        private Long id;

        @JsonProperty("responseSec")
        private Double responseSec;

        public static GetJiraUserStatusDto from(JiraUser jiraUser) {
            return GetJiraUserStatusDto.builder()
                    .id(jiraUser.getId())
                    .responseSec(
                            jiraUser.getJiraIssueResponseList()
                                    .stream()
                                    .mapToLong(JiraIssueResponse::getResponseSecond)
                                    .average()
                                    .orElse(0.0))
                    .build();
        }

    }
}
