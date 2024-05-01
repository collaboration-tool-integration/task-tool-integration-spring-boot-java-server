package com.pbl.tasktoolintegration.jira.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class JiraIssueType {
    // DB 고유 ID
    @Id
    @GeneratedValue
    private Long id;
    // 지라 조직 내 ID
    @Column(nullable = false)
    private Long jiraId;
    // 이슈 타입명
    @Column(nullable = false)
    private String name;
    // 번역되지 않은 이슈 타입명
    private String untranslatedName;
    // 이슈 타입 설명
    private String description;
    // 이슈 타입 아이콘 URL
    private String iconUrl;
    // 서브테스크 타입 여부
    @Column(nullable = false)
    private Boolean subtask;
    // 지라 조직 내 이슈 타입 아이콘 ID
    private Long avatarId;
    // 이슈 타입 계층 레벨
    private Integer hierarchyLevel;
    // Jira Project
    @ManyToOne
    @JoinColumn(name = "jira_project_id")
    private JiraProject jiraProject;
    // Jira Issue
    @OneToMany(mappedBy = "jiraIssueType")
    private List<JiraIssue> jiraIssueList = new ArrayList<>();
    // Issue History
    @OneToMany(mappedBy = "jiraIssueType")
    private List<JiraIssueHistory> jiraIssueHistoryList = new ArrayList<>();
}