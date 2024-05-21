package com.pbl.tasktoolintegration.jira.repository;

import com.pbl.tasktoolintegration.jira.entity.JiraIssue;
import com.pbl.tasktoolintegration.jira.entity.JiraProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JiraIssueRepository extends JpaRepository<JiraIssue, Long> {
    Optional<JiraIssue> findByJiraId(Long jiraId);
    Optional<JiraIssue> findByJiraIdAndJiraProject(Long jiraId, JiraProject jiraProject);

    // 전체 이슈 대상
    List<JiraIssue> findAllByJiraProjectAndDescriptionNotNullAndJiraCommentListNotEmpty(JiraProject jiraProject);
    List<JiraIssue> findAllByDescriptionNotNullAndJiraCommentListNotEmpty();

    // 생성일자 기준 종료/시작 일자 지정하여 이슈 조회
    List<JiraIssue> findAllByJiraProjectAndDescriptionNotNullAndJiraCommentListNotEmptyAndCreatedBetween(JiraProject jiraProject, LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<JiraIssue> findAllByDescriptionNotNullAndJiraCommentListNotEmptyAndCreatedBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<JiraIssue> findAllByJiraProjectAndAssigneeUserNotNullAndParentIssueNotNullAndDuedateNotNull(JiraProject jiraProject);
    List<JiraIssue> findAllByAssigneeUserNotNullAndParentIssueNotNullAndDuedateNotNull();

    List<JiraIssue> findAllByJiraProjectAndAssigneeUserNotNullAndDuedateNotNull(JiraProject jiraProject);
    List<JiraIssue> findAllByAssigneeUserNotNullAndDuedateNotNull();
}
