package com.pbl.tasktoolintegration.jira.repository;

import com.pbl.tasktoolintegration.jira.entity.JiraIssueType;
import com.pbl.tasktoolintegration.jira.entity.JiraProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JiraIssueTypeRepository extends JpaRepository<JiraIssueType, Long> {
    Optional<JiraIssueType> findByJiraId(Long jiraId);
    Optional<JiraIssueType> findByJiraIdAndJiraProject(Long jiraId, JiraProject jiraProject);
}
