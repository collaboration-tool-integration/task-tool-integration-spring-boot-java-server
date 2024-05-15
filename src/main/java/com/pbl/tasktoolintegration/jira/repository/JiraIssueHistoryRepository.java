package com.pbl.tasktoolintegration.jira.repository;

import com.pbl.tasktoolintegration.jira.entity.JiraIssueHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;


public interface JiraIssueHistoryRepository extends JpaRepository<JiraIssueHistory, Long> {
    Optional<JiraIssueHistory> findByJiraIdAndUpdated(Long jiraId, LocalDateTime updated);
}
