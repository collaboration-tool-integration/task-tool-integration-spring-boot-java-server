package com.pbl.tasktoolintegration.jira;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/jira")
public class JiraController {
    private final JiraService jiraService;

    @GetMapping("")
    public ResponseEntity<Void> jiraSync() {
        jiraService.syncJiraUser();
        jiraService.syncJiraProject();

        return ResponseEntity.ok().build();
    }
}
