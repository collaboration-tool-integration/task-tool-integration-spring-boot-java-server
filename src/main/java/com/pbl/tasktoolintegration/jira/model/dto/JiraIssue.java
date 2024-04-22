package com.pbl.tasktoolintegration.jira.model.dto;

import com.atlassian.adf.model.node.Doc;
import com.atlassian.adf.model.node.Node;
import com.atlassian.adf.model.node.type.DocContent;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JiraIssueFields {
        @JsonProperty("watcher")
        private List<JiraIssueWatcher> watcher;

        @JsonProperty("assignee")
        private JiraUser assignee;

        @JsonProperty("description")
        private Doc description;

        @JsonProperty("comment")
        private JiraIssueComment comment;

        @JsonProperty("created")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        private LocalDateTime created;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class JiraIssueWatcher {
            @JsonProperty("isWatching")
            private boolean isWatching;

            @JsonProperty("watchCount")
            private int watchCount;

            @JsonProperty("watchers")
            private List<JiraUser> watchers;
        }

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class JiraIssueComment extends JiraPaging {
            @JsonProperty("comments")
            private List<JiraComment> comments;
        }
    }
}
