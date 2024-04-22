package com.pbl.tasktoolintegration.jira;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/jira")
public class JiraController {
    private final JiraService jiraService;

    @GetMapping("/user")
    public String getUser() {
        jiraService.getJiraUserList(0, 1000);
        jiraService.getSingleIssue("KAN-1");

        return "ok";
    }
}
