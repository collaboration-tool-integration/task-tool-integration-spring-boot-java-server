package com.pbl.tasktoolintegration.jira.entity;

import com.pbl.tasktoolintegration.jira.model.dto.JiraIssue;
import com.pbl.tasktoolintegration.jira.model.dto.JiraUser;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Data
@Entity
public class JiraCommentHistory {
    // DB 고유 ID
    @Id
    @GeneratedValue
    private Long id;

    // 지라 조직 내 ID
    private Long jiraId;

    // 댓글 내용
    @Column(nullable = false)
    @Lob
    private String body;

    // 생성 일자
    @Column(nullable = false)
    private LocalTime created;

    // 업데이트 일자
    private LocalTime updated;

    @ManyToOne
    @JoinColumn(name = "jira_comment_id")
    private JiraComment jiraComment;

    // Jira Issue
    @ManyToOne
    @JoinColumn(name = "jira_issue_id")
    private JiraIssue jiraIssue;

    @ManyToOne
    @JoinColumn(name = "author_user_id")
    private JiraUser authorUserId;

    @ManyToOne
    @JoinColumn(name = "update_author_id")
    private JiraUser updateAuthorId;
}