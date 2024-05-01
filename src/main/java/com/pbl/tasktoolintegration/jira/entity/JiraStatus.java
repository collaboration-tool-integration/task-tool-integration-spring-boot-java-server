package com.pbl.tasktoolintegration.jira.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.ArrayList;

@Data
@Entity
@Table(name = "JiraStatus", indexes = @Index(columnList = "jiraProjectId, jiraStatusCategoryId", unique = true))
public class JiraStatus {
    // DB 고유 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 상태명
    @Column(nullable = false)
    private String name;
    // 상태 설명
    private String description;

    // Jira Project
    @ManyToOne
    @JoinColumn(name = "jiraProjectId")
    private JiraProject jiraProject;

    // Jira Status Category
    @ManyToOne
    @JoinColumn(name = "jiraStatusCategoryId")
    private JiraStatusCategory jiraStatusCategory;

    // Jira Issue
    @OneToMany(mappedBy = "jiraStatus")
    private List<JiraIssue> jiraIssueList = new ArrayList<>();

    // Issue History
    @OneToMany(mappedBy = "jiraStatus")
    private List<JiraIssueHistory> jiraIssueHistoryList = new ArrayList<>();
}