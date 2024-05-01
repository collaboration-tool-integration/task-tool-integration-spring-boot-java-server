package com.pbl.tasktoolintegration.jira.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.ArrayList;

@Data
@Entity
@Table(name = "JiraUser", indexes = @Index(name = "idx_jira_account_id", columnList = "jiraAccountId", unique = true))
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


    @OneToMany(mappedBy = "leadUser")
    private List<JiraProject> leadJiraProjectList = new ArrayList<>();

    @OneToMany(mappedBy = "archivedUser")
    private List<JiraProject> archivedJiraProjectList = new ArrayList<>();

    @OneToMany(mappedBy = "deletedUser")
    private List<JiraProject> deletedJiraProjectList = new ArrayList<>();


    @OneToMany(mappedBy = "assigneeUser")
    private List<JiraIssue> assigneeJiraIssueList = new ArrayList<>();

    @OneToMany(mappedBy = "creatorUser")
    private List<JiraIssue> creatorJiraIssueList = new ArrayList<>();

    @OneToMany(mappedBy = "assigneeUser")
    private List<JiraIssueHistory> assigneeJiraIssueHistoryList = new ArrayList<>();

    @OneToMany(mappedBy = "creatorUser")
    private List<JiraIssueHistory> creatorJiraIssueHistoryList = new ArrayList<>();

    @OneToMany(mappedBy = "authorUser")
    private List<JiraComment> authorJiraCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "updateAuthorUser")
    private List<JiraComment> updateJiraCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "authorUser")
    private List<JiraCommentHistory> authorJiraCommentHistoryList = new ArrayList<>();

    @OneToMany(mappedBy = "updateAuthorUser")
    private List<JiraCommentHistory> updateAuthorJiraCommentHistoryList = new ArrayList<>();
}