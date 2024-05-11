package com.pbl.tasktoolintegration.jira.repository;

import com.pbl.tasktoolintegration.jira.entity.JiraIssue;
import com.pbl.tasktoolintegration.jira.entity.JiraProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JiraIssueRepository extends JpaRepository<JiraIssue, Long> {
    Optional<JiraIssue> findByJiraId(Long jiraId);
    Optional<JiraIssue> findByJiraIdAndJiraProject(Long jiraId, JiraProject jiraProject);

    List<JiraIssue> findAllByJiraProjectAndDescriptionNotNullAndJiraCommentListNotEmpty(JiraProject jiraProject);
    List<JiraIssue> findAllByDescriptionNotNullAndJiraCommentListNotEmpty();

    List<JiraIssue> findAllByJiraProjectAndAssigneeUserNotNullAndParentIssueNotNullAndDuedateNotNull(JiraProject jiraProject);
    List<JiraIssue> findAllByAssigneeUserNotNullAndParentIssueNotNullAndDuedateNotNull();

    List<JiraIssue> findAllByJiraProjectAndAssigneeUserNotNullAndDuedateNotNull(JiraProject jiraProject);
    List<JiraIssue> findAllByAssigneeUserNotNullAndDuedateNotNull();
}
