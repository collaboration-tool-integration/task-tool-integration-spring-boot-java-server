package com.pbl.tasktoolintegration.jira;

import com.pbl.tasktoolintegration.jira.model.dto.JiraUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/jira")
public class JiraController {
    private final JiraService jiraService;

    @GetMapping("/user-synced")
    public ResponseEntity<String> syncJiraUser() {
        jiraService.syncJiraUser();
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/issue-synced")
    public ResponseEntity<String> syncJiraIssue() {
        jiraService.syncJiraIssue();
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/user-statuses")
    public ResponseEntity<List<JiraUserDto.GetJiraUserStatusDto>> getAllUserStatuses() {
        return ResponseEntity.ok(jiraService.getAllUserStatuses());
    }
}
