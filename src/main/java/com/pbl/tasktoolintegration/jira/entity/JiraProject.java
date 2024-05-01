package com.pbl.tasktoolintegration.jira.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Data
@Entity
public class JiraProject {
    // DB 고유 ID
    @Id
    @GeneratedValue
    private Long id;
    // 지라 내 UUID
    @Column(nullable = false)
    private String jiraUuid;
    // 지라 조직 내 ID
    @Column(nullable = false)
    private Long jiraId;
    // 프로젝트명
    @Column(nullable = false)
    private String name;
    // 프로젝트 설명
    private String description;
    // 프로젝트 타입 키
    private String typeKey;
    // 프로젝트 로고 URL
    private String iconUrl;
    // 아카이브 여부
    @Column(nullable = false)
    private Boolean archived;
    // 아카이브 일시
    private LocalDateTime archivedDate;
    // 비공개 여부
    @Column(nullable = false)
    private Boolean isPrivate;
    // 단순화 여부
    @Column(nullable = false)
    private Boolean simplified;
    // 삭제 여부
    @Column(nullable = false)
    private Boolean deleted;
    // 삭제 일시
    private LocalDateTime deletedDate;
    // Jira User
    @ManyToOne
    @JoinColumn(name = "lead_user_id")
    private JiraUser leadUser;
    // Jira User
    @ManyToOne
    @JoinColumn(name = "archived_user_id")
    private JiraUser archivedUser;
    // Jira User
    @ManyToOne
    @JoinColumn(name = "deleted_user_id")
    private JiraUser deletedUser;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private JiraOrganization jiraOrganization;

    // Jira Issue Type
    @OneToMany(mappedBy = "jiraProject")
    private List<JiraIssueType> jiraIssueTypeList = new ArrayList<>();

    // Jira Issue
    @OneToMany(mappedBy = "jiraProject")
    private List<JiraIssue> jiraIssueList = new ArrayList<>();

    @OneToMany(mappedBy = "jiraProject")
    private List<JiraStatus> jiraStatus;
}