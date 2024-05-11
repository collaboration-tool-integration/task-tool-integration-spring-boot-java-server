package com.pbl.tasktoolintegration.jira.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "JiraIssueHistory", indexes = @Index(columnList = "updated, jiraIssueId", unique = true))
public class JiraIssueHistory {
    // DB 고유 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, nullable = false)
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
    @Column(nullable = false, columnDefinition = "JSON")
    @JdbcTypeCode(SqlTypes.JSON)
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
    @JoinColumn(name = "parentIssueId")
    private JiraIssue parentIssue;

    // Jira Issue
    @ManyToOne
    @JoinColumn(name = "jiraIssueId")
    private JiraIssue jiraIssue;

    // Jira User
    @ManyToOne
    @JoinColumn(name = "creatorUserId")
    private JiraUser creatorUser;

    // Jira User
    @ManyToOne
    @JoinColumn(name = "assigneeUserId")
    private JiraUser assigneeUser;

    // Jira Issue Type
    @ManyToOne
    @JoinColumn(name = "issueTypeId")
    private JiraIssueType jiraIssueType;

    @ManyToOne
    @JoinColumn(name = "issueStatusId")
    private JiraStatus jiraStatus;
}