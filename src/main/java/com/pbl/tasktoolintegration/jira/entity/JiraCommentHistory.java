package com.pbl.tasktoolintegration.jira.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "JiraCommentHistory", indexes = @Index(columnList = "updated, jiraCommentId", unique = true))
public class JiraCommentHistory {
    // DB 고유 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 지라 조직 내 ID
    private Long jiraId;

    // 댓글 내용
    @Column(nullable = false)
    @Lob
    private String body;

    // 생성 일자
    @Column(nullable = false)
    private LocalDateTime created;

    // 업데이트 일자
    private LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name = "jiraCommentId")
    private JiraComment jiraComment;

    // Jira Issue
    @ManyToOne
    @JoinColumn(name = "jiraIssueId")
    private JiraIssue jiraIssue;

    @ManyToOne
    @JoinColumn(name = "authorUserId")
    private JiraUser authorUser;

    @ManyToOne
    @JoinColumn(name = "updateAuthorUserId")
    private JiraUser updateAuthorUser;
}