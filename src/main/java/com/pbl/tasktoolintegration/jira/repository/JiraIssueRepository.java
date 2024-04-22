package com.pbl.tasktoolintegration.jira.repository;

import com.pbl.tasktoolintegration.jira.entity.JiraIssue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JiraIssueRepository extends JpaRepository<JiraIssue, Long> {
    Optional<JiraIssue> findByJiraId(Long jiraId);
}
