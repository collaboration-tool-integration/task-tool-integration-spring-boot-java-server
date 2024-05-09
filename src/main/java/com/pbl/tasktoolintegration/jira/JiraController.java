package com.pbl.tasktoolintegration.jira;

import com.pbl.tasktoolintegration.jira.model.dto.JiraResponseTimeDto;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/response-time")
    public ResponseEntity<List<JiraResponseTimeDto>> getResponseTimeByProject(
            @RequestParam(value = "projectId", required = false) Long projectId
    ) {
        return ResponseEntity.ok(jiraService.getJiraUserResponseTime(projectId));
    }
}
