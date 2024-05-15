package com.pbl.tasktoolintegration.jira.repository;

import com.pbl.tasktoolintegration.jira.entity.JiraCommentHistory;
import com.pbl.tasktoolintegration.jira.entity.JiraIssueHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;


public interface JiraCommentHistoryRepository extends JpaRepository<JiraCommentHistory, Long> {
    Optional<JiraCommentHistory> findByJiraIdAndUpdated(Long jiraId, LocalDateTime updated);

}
