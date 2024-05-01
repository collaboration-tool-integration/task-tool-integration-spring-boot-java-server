package com.pbl.tasktoolintegration.jira.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "JiraIssue", indexes = @Index(columnList = "jiraProjectId, jiraId", unique = true))
public class JiraIssue {
    // DB 고유 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne
    @JoinColumn(name = "assigneeUserId")
    private JiraUser assigneeUser;

    @ManyToOne
    @JoinColumn(name = "creatorUserId")
    private JiraUser creatorUser;

    // Jira Issue Type
    @ManyToOne
    @JoinColumn(name = "issueTypeId")
    private JiraIssueType jiraIssueType;

    // Jira Project
    @ManyToOne
    @JoinColumn(name = "jiraProjectId")
    private JiraProject jiraProject;

    @ManyToOne
    @JoinColumn(name = "issueStatusId")
    private JiraStatus jiraStatus;

    // Jira Issue
    @ManyToOne
    @JoinColumn(name = "parentIssueId")
    private JiraIssue parentIssue;

    @OneToMany(mappedBy = "parentIssue")
    private List<JiraIssue> childIssueList = new ArrayList<>();

    @OneToMany(mappedBy = "jiraIssue")
    private List<JiraComment> jiraCommentList = new ArrayList<>();

    // Issue History
    @OneToMany(mappedBy = "jiraIssue")
    private List<JiraIssueHistory> jiraIssueHistoryList = new ArrayList<>();

    // Comment History
    @OneToMany(mappedBy = "jiraIssue")
    private List<JiraCommentHistory> jiraCommentHistoryList = new ArrayList<>();

}