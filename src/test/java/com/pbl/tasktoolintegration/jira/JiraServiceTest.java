package com.pbl.tasktoolintegration.jira;

import com.atlassian.adf.model.Element;
import com.atlassian.adf.model.node.Doc;
import com.atlassian.adf.model.node.Mention;
import com.atlassian.adf.model.node.Node;
import com.atlassian.adf.model.node.Paragraph;
import com.atlassian.adf.model.node.type.DocContent;
import com.atlassian.adf.model.node.type.ListItemContent;
import com.pbl.tasktoolintegration.jira.config.JiraConfiguration;
import com.pbl.tasktoolintegration.jira.model.dto.JiraComment;
import com.pbl.tasktoolintegration.jira.model.dto.JiraIssue;
import com.pbl.tasktoolintegration.jira.model.dto.JiraIssueList;
import com.pbl.tasktoolintegration.jira.model.dto.JiraUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JiraConfiguration.class, JiraService.class})
class JiraServiceTest {
    @Autowired
    JiraService jiraService;
}