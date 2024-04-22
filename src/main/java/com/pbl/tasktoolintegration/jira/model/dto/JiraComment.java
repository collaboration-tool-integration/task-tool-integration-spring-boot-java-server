package com.pbl.tasktoolintegration.jira.model.dto;

import com.atlassian.adf.model.node.Doc;
import com.atlassian.adf.model.node.Node;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class JiraComment {
    @JsonProperty("id")
    private long id;

    @JsonProperty("author")
    private JiraUser author;

    @JsonProperty("body")
    private Doc body;

    @JsonProperty("created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private LocalDateTime created;
}
