package com.pbl.tasktoolintegration.jira.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
public class JiraIssueHistory {
    // DB 고유 ID
    @Id
    @GeneratedValue
    private Long id;

    // 지라 조직 내 ID
    @Column(nullable = false)
    private Long jiraId;

    // 이슈 키
    @Column(nullable = false)
    private String key;

    // 이슈 요약 (제목)
    @Column(nullable = false)
    private String summary;

    // 마감일
    private LocalDate duedate;

    // 이슈 상세 내용
    @Lob
    private String description;

    // 이슈 중요도
    private Integer issuePriority;

    // 생성일자
    @Column(nullable = false)
    private LocalDateTime created;

    // 업데이트 일자
    private LocalDateTime updated;

    // Jira Issue
    @ManyToOne
    @JoinColumn(name = "parent_issue_id")
    private JiraIssue parentIssue;

    // Jira Issue
    @ManyToOne
    @JoinColumn(name = "jira_issue_id")
    private JiraIssue jiraIssue;

    // Jira User
    @ManyToOne
    @JoinColumn(name = "creator_user_id")
    private JiraUser creatorUserId;

    // Jira User
    @ManyToOne
    @JoinColumn(name = "assignee_user_id")
    private JiraUser assigneeUserId;

    // Jira Issue Type
    @ManyToOne
    @JoinColumn(name = "issue_type_id")
    private JiraIssueType jiraIssueType;

    @ManyToOne
    @JoinColumn(name = "issue_status_id")
    private JiraStatus jiraStatus;
}