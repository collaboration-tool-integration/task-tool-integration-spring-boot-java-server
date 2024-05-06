package com.pbl.tasktoolintegration.jira.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "JiraProject", indexes = @Index(columnList = "orgId, jiraId", unique = true))
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class JiraProject {
    // DB 고유 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Boolean archived = false;
    // 아카이브 일시
    private LocalDateTime archivedDate;
    // 비공개 여부
    @Column(nullable = false)
    private Boolean isPrivate = false;
    // 단순화 여부
    @Column(nullable = false)
    private Boolean simplified = false;
    // 삭제 여부
    private Boolean deleted;
    // 삭제 일시
    private LocalDateTime deletedDate;
    // Jira User
    @ManyToOne
    @JoinColumn(name = "leadUserId")
    private JiraUser leadUser;
    // Jira User
    @ManyToOne
    @JoinColumn(name = "archivedUserId")
    private JiraUser archivedUser;
    // Jira User
    @ManyToOne
    @JoinColumn(name = "deletedUserId")
    private JiraUser deletedUser;

    @ManyToOne
    @JoinColumn(name = "orgId")
    private JiraOrganization jiraOrganization;

    // Jira Issue Type
    @OneToMany(mappedBy = "jiraProject")
    private List<JiraIssueType> jiraIssueTypeList = new ArrayList<>();

    // Jira Issue
    @OneToMany(mappedBy = "jiraProject")
    private List<JiraIssue> jiraIssueList = new ArrayList<>();

    @OneToMany(mappedBy = "jiraProject")
    private List<JiraStatus> jiraStatus;

    @OneToMany(mappedBy = "jiraProject", cascade = CascadeType.PERSIST)
    private List<JiraProjectKey> jiraProjectKeyList;


    public void setJiraProjectKeyList(List<JiraProjectKey> jiraProjectKeyList) {
        this.jiraProjectKeyList = jiraProjectKeyList;
    }
}