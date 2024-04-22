package com.pbl.tasktoolintegration.jira.entity;

import com.pbl.tasktoolintegration.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class JiraIssue extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "jiraId")
    private Long jiraId;

    @Column(name = "key", length = 128)
    private String key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigneeJiraUserId")
    private JiraUser assigneeJiraUser;

    @Column(name = "issueCreatedAt")
    private LocalDateTime taskCreatedAt;

    @OneToMany(mappedBy = "jiraIssue")
    private List<JiraIssueResponse> jiraIssueResponseList = new ArrayList<>();
}
