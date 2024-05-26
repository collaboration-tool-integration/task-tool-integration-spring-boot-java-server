package com.pbl.tasktoolintegration.jira.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "JiraStatus", indexes = @Index(columnList = "jiraId, jiraProjectId", unique = true))
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class JiraStatus {
    // DB 고유 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, nullable = false)
    private Long id;

    // 지라 조직 내 ID
    @Column(nullable = false)
    private Long jiraId;

    // 상태명
    @Column(nullable = false)
    private String name;

    // 상태 설명
    private String description;

    // 카테고리
    @Column(nullable = false)
    private String jiraStatusCategory;

    // Jira Issue Type
    @ManyToOne
    @JoinColumn(name = "jiraIssueTypeId")
    private JiraIssueType jiraIssueType;

    // Jira Project
    @ManyToOne
    @JoinColumn(name = "jiraProjectId")
    private JiraProject jiraProject;

    // Jira Issue
    @OneToMany(mappedBy = "jiraStatus")
    private List<JiraIssue> jiraIssueList = new ArrayList<>();

    // Issue History
    @OneToMany(mappedBy = "jiraStatus")
    private List<JiraIssueHistory> jiraIssueHistoryList = new ArrayList<>();
}