package com.pbl.tasktoolintegration.jira.repository;

import com.pbl.tasktoolintegration.jira.entity.JiraProject;
import com.pbl.tasktoolintegration.jira.entity.JiraStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JiraStatusRepository extends JpaRepository<JiraStatus, Long> {
    Optional<JiraStatus> findByJiraId(Long jiraId);
    Optional<JiraStatus> findByJiraIdAndJiraProject(Long jiraId, JiraProject jiraProject);
}
