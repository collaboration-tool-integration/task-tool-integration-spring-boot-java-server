package com.pbl.tasktoolintegration.jira.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "JiraComment", indexes = @Index(columnList = "jiraIssueId, jiraId", unique = true))
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JiraComment {
    // DB 고유 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 지라 조직 내 ID
    private Long jiraId;

    // 댓글 내용
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private String body;

    // 생성 일자
    @Column(nullable = false)
    private LocalDateTime created;

    // 업데이트 일자
    private LocalDateTime updated;

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

    // Comment History
    @OneToMany(mappedBy = "jiraComment")
    private List<JiraCommentHistory> jiraCommentHistoryList = new ArrayList<>();
}