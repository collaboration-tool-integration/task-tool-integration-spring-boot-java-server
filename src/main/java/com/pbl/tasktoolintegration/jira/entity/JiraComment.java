package com.pbl.tasktoolintegration.jira.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class JiraComment {
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
    private LocalTime created;

    // 업데이트 일자
    private LocalTime updated;

    // Jira Issue
    @ManyToOne
    @JoinColumn(name = "jira_issue_id")
    private JiraIssue jiraIssue;

    @ManyToOne
    @JoinColumn(name = "author_user_id")
    private JiraUser authorUser;

    @ManyToOne
    @JoinColumn(name = "update_author_id")
    private JiraUser updateUser;

    // Comment History
    @OneToMany(mappedBy = "jiraComment")
    private List<JiraCommentHistory> jiraCommentHistoryList = new ArrayList<>();
}