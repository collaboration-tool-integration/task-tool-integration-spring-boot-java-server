package com.pbl.tasktoolintegration.jira;

import com.pbl.tasktoolintegration.jira.model.dto.JiraDeadlineDto;
import com.pbl.tasktoolintegration.jira.model.dto.JiraResponseTimeDto;
import com.pbl.tasktoolintegration.jira.model.request.PostJiraWebhookRequest;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/deadline-exceeded")
    public ResponseEntity<List<JiraDeadlineDto>> getDeadlineExceedCountByProject(
            @RequestParam(value = "projectId", required = false) Long projectId,
            @RequestParam(value = "includeParentIssue") boolean includeParentIssue
    ) {
        return ResponseEntity.ok(jiraService.getUserDeadlineExceedInfo(projectId, includeParentIssue));
    }

    @PostMapping("/webhook")
    public ResponseEntity<Boolean> postWebhook(
            @RequestBody PostJiraWebhookRequest requestBody,
            @RequestParam(value = "projectId", required = true) Long projectId
    ) {
        log.info("webhook init");
        jiraService.receiveJiraWebhook(projectId, requestBody);

        return ResponseEntity.ok(true);
    }
}
