package com.pbl.tasktoolintegration.jira.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.ArrayList;

@Data
@Entity
public class JiraStatus {
    // DB 고유 ID
    @Id
    @GeneratedValue
    private Long id;
    // 상태명
    @Column(nullable = false)
    private String name;
    // 상태 설명
    private String description;

    // Jira Project
    @ManyToOne
    @JoinColumn(name = "jira_project_id")
    private JiraProject jiraProject;

    // Jira Status Category
    @ManyToOne
    @JoinColumn(name = "jira_status_category_id")
    private JiraStatusCategory jiraStatusCategory;

    // Jira Issue
    @OneToMany(mappedBy = "jiraStatus")
    private List<JiraIssue> jiraIssueList = new ArrayList<>();

    // Issue History
    @OneToMany(mappedBy = "jiraStatus")
    private List<JiraIssueHistory> jiraIssueHistoryList = new ArrayList<>();
}