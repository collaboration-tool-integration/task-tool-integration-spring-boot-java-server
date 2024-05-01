package com.pbl.tasktoolintegration.jira.repository;

import com.pbl.tasktoolintegration.jira.entity.JiraUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JiraUserRepository extends JpaRepository<JiraUser, Long> {
    Optional<JiraUser> findByJiraAccountId(String jiraAccountId);
}
