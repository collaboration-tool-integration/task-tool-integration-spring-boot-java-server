package com.pbl.tasktoolintegration.jira.repository;

import com.pbl.tasktoolintegration.jira.entity.JiraComment;
import com.pbl.tasktoolintegration.jira.entity.JiraIssueType;
import com.pbl.tasktoolintegration.jira.entity.JiraProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JiraCommentRepository extends JpaRepository<JiraComment, Long> {
    Optional<JiraComment> findByJiraId(Long jiraId);

}
