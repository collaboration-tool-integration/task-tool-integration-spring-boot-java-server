package com.pbl.tasktoolintegration.jira.repository;

import com.pbl.tasktoolintegration.jira.entity.JiraIssueResponse;
import com.pbl.tasktoolintegration.jira.entity.JiraUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JiraIssueResponseRepository extends JpaRepository<JiraIssueResponse, Long> {

}
