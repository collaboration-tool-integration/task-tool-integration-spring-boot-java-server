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

    @Test
    @DisplayName("Jira 단일 이슈 조회 API 호출 결과")
    void getIssue() {
        // Given
        JiraIssue jiraIssue = jiraService.getSingleIssue("KAN-1");

        // Issue Created Time
        LocalDateTime issueCreatedTime = jiraIssue.getFields().getCreated();

        // Issue Assignee
        JiraUser assignee = jiraIssue.getFields().getAssignee();

        // Assignee's First Comment
        JiraComment firstComment = jiraIssue.getFields()
                .getComment()
                .getComments()
                .stream()
                .filter(jiraComment -> jiraComment.getAuthor() != null && jiraComment.getAuthor().getAccountId().equals(assignee.getAccountId()))
                .min(Comparator.comparing(JiraComment::getCreated))
                .orElse(null);

        if (firstComment == null) {
            log.info("Assignee's Comment Not Found");
            return;
        }

        long responseSec = ChronoUnit.SECONDS.between(issueCreatedTime, firstComment.getCreated());
        log.info("Issue First Response Second: " + responseSec);
    }
}