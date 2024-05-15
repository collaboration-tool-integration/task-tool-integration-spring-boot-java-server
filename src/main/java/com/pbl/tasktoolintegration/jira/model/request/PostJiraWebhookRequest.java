package com.pbl.tasktoolintegration.jira.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pbl.tasktoolintegration.jira.model.dto.GetJiraIssueCommentDto;
import com.pbl.tasktoolintegration.jira.model.dto.GetJiraIssueDto;
import com.pbl.tasktoolintegration.jira.model.dto.GetJiraUserDto;
import com.pbl.tasktoolintegration.jira.model.dto.JiraIssueChangeLogDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostJiraWebhookRequest {
    @JsonProperty("timestamp")
    private Long timestamp;

    @JsonProperty("webhookEvent")
    private String webhookEvent;

    @JsonProperty("issue_event_type_name")
    private String issueEventTypeName;

    @JsonProperty("issue")
    private JiraWebhookIssueDto issue;

    @JsonProperty("user")
    @Nullable
    private GetJiraUserDto user;

    @JsonProperty("changelog")
    private JiraWebhookIssueChangeLogDto changeLog;

    @JsonProperty("comment")
    private JiraWebhookCommentDto comment;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JiraWebhookIssueDto {
        @JsonProperty("id")
        private String id;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JiraWebhookIssueChangeLogDto {
        @JsonProperty("id")
        private String id;

        @JsonProperty("items")
        private List<JiraIssueChangeLogDto> items;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JiraWebhookCommentDto {
        @JsonProperty("id")
        private String id;
    }
}
