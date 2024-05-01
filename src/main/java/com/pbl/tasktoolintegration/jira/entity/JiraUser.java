package com.pbl.tasktoolintegration.jira.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.ArrayList;

@Data
@Entity
public class JiraUser {
    // DB 고유 ID
    @Id
    @GeneratedValue
    private Long id;
    // 지라 계정 ID
    @Column(nullable = false)
    private String jiraAccountId;
    // 지라 계정 타입
    @Column(nullable = false)
    private String jiraAccountType;
    // 계정 아바타 URL
    private String avatarUrls;
    // 사용자명
    private String displayName;
    // 활성 사용자 여부
    @Column(nullable = false)
    private Boolean active;

    // Jira Project
    @OneToMany(mappedBy = "jiraUser")
    private List<JiraProject> jiraProjectList = new ArrayList<>();

    @OneToMany(mappedBy = "assigneeJiraUser")
    private List<JiraIssue> assigneeJiraIssueList = new ArrayList<>();

    @OneToMany(mappedBy = "creatorJiraUser")
    private List<JiraIssue> creatorJiraIssueList = new ArrayList<>();

    // Issue History
    @OneToMany(mappedBy = "jiraUser")
    private List<JiraIssueHistory> jiraIssueHistoryList = new ArrayList<>();

    @OneToMany(mappedBy = "assigneeJiraUser")
    private List<JiraIssueHistory> assigneeJiraIssueHistoryList = new ArrayList<>();

    @OneToMany(mappedBy = "creatorJiraUser")
    private List<JiraIssueHistory> creatorJiraIssueHistoryList = new ArrayList<>();

    @OneToMany(mappedBy = "authorUser")
    private List<JiraComment> authorJiraCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "updateUser")
    private List<JiraComment> updateJiraCommentList = new ArrayList<>();

    // Comment History
    @OneToMany(mappedBy = "jiraUser")
    private List<JiraCommentHistory> jiraCommentHistoryList = new ArrayList<>();
}