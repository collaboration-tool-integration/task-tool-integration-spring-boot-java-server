package com.pbl.tasktoolintegration.jira.model.dto;

import com.atlassian.adf.model.node.Doc;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class JiraIssueList extends JiraPaging {
    @JsonProperty("issues")
    private List<JiraIssue> issues = new ArrayList<>();
}
