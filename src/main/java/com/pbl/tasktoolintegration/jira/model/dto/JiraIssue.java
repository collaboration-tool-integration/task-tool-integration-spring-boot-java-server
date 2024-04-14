package com.pbl.tasktoolintegration.jira.model.dto;

import com.atlassian.adf.model.node.Doc;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class JiraIssue {
    @JsonProperty("id")
    private long id;

    @JsonProperty("key")
    private String key;

    @JsonProperty("fields")
    private JiraIssueFields fields;

    @JsonProperty("description")
    private JiraIssueDescription description;

    @JsonProperty("comment")
    private List<JiraIssueComment> comment;

    public static class JiraIssueFields {
        @JsonProperty("watcher")
        private List<JiraIssueWatcher> watcher;

        public static class JiraIssueWatcher {
            @JsonProperty("isWatching")
            private boolean isWatching;

            @JsonProperty("watchCount")
            private int watchCount;

            @JsonProperty("watchers")
            private List<JiraUser> watchers;
        }
    }

    public static class JiraIssueDescription {
        @JsonProperty("type")
        private String type;

        @JsonProperty("version")
        private int version;

        @JsonProperty("content")
        private List<Doc> content;
    }

    public static class JiraIssueComment {
        @JsonProperty("author")
        private JiraUser author;

        @JsonProperty("body")
        private JiraIssueCommentBody body;

        @JsonProperty("created")
        private LocalDateTime created;

        @JsonProperty("id")
        private long id;

        public static class JiraIssueCommentBody {
            @JsonProperty("type")
            private String type;

            @JsonProperty("version")
            private int version;

            @JsonProperty("content")
            private List<Doc> content;
        }
    }
}
