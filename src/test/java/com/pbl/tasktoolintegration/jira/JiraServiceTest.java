package com.pbl.tasktoolintegration.jira;

import com.pbl.tasktoolintegration.jira.config.JiraConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JiraConfiguration.class, JiraService.class})
class JiraServiceTest {
    @Autowired
    JiraService jiraService;

}