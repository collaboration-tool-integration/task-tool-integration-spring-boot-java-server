package com.pbl.tasktoolintegration.jira.entity;

import com.pbl.tasktoolintegration.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class JiraUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "jiraId", length = 512)
    private String jiraId;

    @Column(name = "name", length = 256)
    private String name;

    @Column(name = "email", length = 512)
    private String email;

    @OneToMany(mappedBy = "assigneeJiraUser")
    private List<JiraIssue> jiraIssueList = new ArrayList<>();

    @OneToMany(mappedBy = "jiraUser")
    private List<JiraIssueResponse> jiraIssueResponseList = new ArrayList<>();
}
