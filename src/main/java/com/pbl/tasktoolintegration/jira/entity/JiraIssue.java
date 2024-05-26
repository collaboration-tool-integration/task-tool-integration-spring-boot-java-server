package com.pbl.tasktoolintegration.jira.entity;

import com.atlassian.adf.jackson2.AdfJackson2;
import com.atlassian.adf.model.node.Doc;
import com.atlassian.adf.model.node.Mention;
import com.pbl.tasktoolintegration.jira.model.dto.GetJiraIssueDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Entity
@Table(name = "JiraIssue", indexes = @Index(columnList = "jiraProjectId, jiraId", unique = true))
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class JiraIssue {
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
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private String description;

    // 이슈 중요도
    private String issuePriority;

    // 생성일자
    @Column(nullable = false)
    private LocalDateTime created;

    // 업데이트 일자
    private LocalDateTime updated;

    // 이슈 해결 일자
    private LocalDateTime resolutionDate;

    @ManyToOne
    @JoinColumn(name = "assigneeUserId")
    private JiraUser assigneeUser;

    @ManyToOne
    @JoinColumn(name = "creatorUserId")
    private JiraUser creatorUser;

    @ManyToOne
    @JoinColumn(name = "reportUserId")
    private JiraUser reportUser;

    // Jira Issue Type
    @ManyToOne
    @JoinColumn(name = "issueTypeId")
    private JiraIssueType jiraIssueType;

    // Jira Project
    @ManyToOne
    @JoinColumn(name = "jiraProjectId")
    private JiraProject jiraProject;

    @ManyToOne
    @JoinColumn(name = "issueStatusId")
    private JiraStatus jiraStatus;

    // Jira Issue
    @ManyToOne
    @JoinColumn(name = "parentIssueId")
    private JiraIssue parentIssue;

    @OneToMany(mappedBy = "parentIssue")
    private List<JiraIssue> childIssueList = new ArrayList<>();

    @OneToMany(mappedBy = "jiraIssue", cascade = CascadeType.PERSIST)
    private List<JiraComment> jiraCommentList = new ArrayList<>();

    // Issue History
    @OneToMany(mappedBy = "jiraIssue")
    private List<JiraIssueHistory> jiraIssueHistoryList = new ArrayList<>();

    // Comment History
    @OneToMany(mappedBy = "jiraIssue")
    private List<JiraCommentHistory> jiraCommentHistoryList = new ArrayList<>();

    public void setJiraCommentList(List<JiraComment> jiraCommentList) {
        this.jiraCommentList = jiraCommentList;
    }

    public Doc getDescription() {
        return new AdfJackson2().unmarshall(description);
    }

    public List<String> getRelatedUserIdList() {
        ArrayList<String> jiraUserList = new ArrayList<>();

        // 담당자 추가
        jiraUserList.add(assigneeUser.getJiraAccountId());

        // 멘션 노드 전체 조회
        getDescription().allNodesOfType(Mention.class)
                .map(Mention::id)
                .distinct()
                .toList();


        return jiraUserList;
    }

    public JiraIssueHistory toHistory() {
        return JiraIssueHistory.builder()
                .jiraId(jiraId)
                .key(key)
                .summary(summary)
                .duedate(duedate)
                .description(description)
                .issuePriority(issuePriority)
                .created(created)
                .updated(updated)
                .resolutionDate(resolutionDate)
                .assigneeUser(assigneeUser)
                .creatorUser(creatorUser)
                .reportUser(reportUser)
                .jiraIssueType(jiraIssueType)
                .jiraProject(jiraProject)
                .jiraStatus(jiraStatus)
                .parentIssue(parentIssue)
                .jiraIssue(this)
                .build();
    }

    public void updateByDto(GetJiraIssueDto jiraIssueDto,
                            JiraIssue parentIssue,
                            JiraIssueType jiraIssueType,
                            JiraStatus jiraStatus,
                            JiraUser assigneeUser,
                            JiraUser creatorUser,
                            JiraUser reportUser
    ) {
        AdfJackson2 adfJackson2 = new AdfJackson2();

        this.summary = jiraIssueDto.getFields().getSummary();
        this.parentIssue = parentIssue;
        this.duedate = jiraIssueDto.getFields().getDuedate();
        this.description = jiraIssueDto.getFields().getDescription() != null ? adfJackson2.marshall(jiraIssueDto.getFields().getDescription()) : null;
        this.issuePriority = jiraIssueDto.getFields().getPriority().getName();
        this.created = jiraIssueDto.getFields().getCreated();
        this.updated = jiraIssueDto.getFields().getUpdated();
        this.resolutionDate = jiraIssueDto.getFields().getResolutionDate();
        this.assigneeUser = assigneeUser;
        this.creatorUser = creatorUser;
        this.reportUser = reportUser;
        this.jiraIssueType = jiraIssueType;
        this.jiraStatus = jiraStatus;
    }
}