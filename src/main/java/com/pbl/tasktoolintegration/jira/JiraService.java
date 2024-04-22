package com.pbl.tasktoolintegration.jira;

import com.atlassian.adf.model.Element;
import com.atlassian.adf.model.node.Mention;
import com.atlassian.adf.model.node.Node;
import com.atlassian.adf.model.node.Text;
import com.atlassian.adf.model.node.type.DocContent;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pbl.tasktoolintegration.jira.model.dto.JiraIssue;
import com.pbl.tasktoolintegration.jira.model.dto.JiraUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class JiraService {
    private final WebClient jiraWebClient;

    public List<JiraUser> getJiraUserList(int startAt, int maxResult) {
        List<JiraUser> userList = jiraWebClient
                .get()
                .uri(uriBuilder -> uriBuilder.pathSegment("users", "search")
                        .queryParam("startAt", startAt)
                        .queryParam("maxResult", maxResult)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<JiraUser>> () {})
                .block();

        if (userList != null) {
            userList.stream()
                    .filter((user) -> user.getAccountType().equals(JiraConstants.JiraAccountType.atlassian))
                    .forEach((user) -> {
                        log.info(user.getAccountId() + ". " + user.getDisplayName());
                    });

        }
        return userList;
    }

    public JiraIssue getSingleIssue(String issueId) {
        JiraIssue result = jiraWebClient
                .get()
                .uri(uriBuilder -> uriBuilder.pathSegment("issue")
                        .path(issueId)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<JiraIssue>() {
                })
                .block();

        return result;
    }
}
