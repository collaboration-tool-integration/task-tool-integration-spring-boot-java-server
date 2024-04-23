package com.pbl.tasktoolintegration.jira.entity;

import com.pbl.tasktoolintegration.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class JiraIssueResponse extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jiraIssueId")
    private JiraIssue jiraIssue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jiraUserId")
    private JiraUser jiraUser;

    @Column(name = "responseSecond")
    private Long responseSecond;
}
