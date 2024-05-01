package com.pbl.tasktoolintegration.jira.repository;

import com.pbl.tasktoolintegration.jira.entity.JiraProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JiraProjectRepository extends JpaRepository<JiraProject, Long> {
    Optional<JiraProject> findByJiraId(Long jiraId);
}
