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
import com.pbl.tasktoolintegration.jira.model.dto.JiraIssueList;
import com.pbl.tasktoolintegration.jira.model.dto.JiraUser;
import com.pbl.tasktoolintegration.jira.repository.JiraIssueRepository;
import com.pbl.tasktoolintegration.jira.repository.JiraUserRepository;
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
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class JiraService {
    private final WebClient jiraWebClient;
    private final JiraUserRepository jiraUserRepository;
    private final JiraIssueRepository jiraIssueRepository;

    /**
     * Jira 유저를 DB와 동기화하기
     */
    @Transactional
    public void syncJiraUser() {
        // Jira 유저 목록 API로 조회
        List<JiraUser> jiraUserList = getJiraUserList();

        // 넘어온 목록 loop
        for (JiraUser jiraUser : jiraUserList) {
            // 실제 사용자가 아니라면 pass
            if (!jiraUser.getAccountType().equals(JiraConstants.JiraAccountType.atlassian)) return;

            // DB에서 해당 ID를 가진 user 찾기
            com.pbl.tasktoolintegration.jira.entity.JiraUser dbJiraUser = findUserEntityByJiraId(jiraUser.getAccountId())
                    .orElse(null);

            // 이미 있다면 패스
            if (dbJiraUser != null) continue;

            // 없다면 생성 후 저장
            com.pbl.tasktoolintegration.jira.entity.JiraUser newUser = com.pbl.tasktoolintegration.jira.entity.JiraUser.builder()
                    .name(jiraUser.getDisplayName())
                    .jiraId(jiraUser.getAccountId())
                    .email(jiraUser.getEmailAddress())
                    .build();

            jiraUserRepository.save(newUser);
        }
    }

    /**
     * Jira 이슈를 DB와 동기화하기
     * TODO: 언급자 목록 + 댓글 목록으로 IssueResponse 테이블 저장 구현 필요
     * TODO: 하위 이슈에 대한 대응이 필요
     */
    @Transactional
    public void syncJiraIssue() {
        // Jira 유저 목록 API로 조회
        List<JiraIssue> jiraIssueList = getIssueList();

        // 넘어온 목록 loop
        for (JiraIssue jiraIssue : jiraIssueList) {
            // DB에서 해당 ID를 가진 user 찾기
            com.pbl.tasktoolintegration.jira.entity.JiraIssue dbJiraIssue = findIssueEntityByJiraId(jiraIssue.getId())
                    .orElse(null);

            // 이미 있다면 패스
            if (dbJiraIssue != null) continue;

            // Field가 정상적이지 않으면 패스
            if (jiraIssue.getFields() == null) continue;

            // 이슈 할당자가 있을 경우 할당자도 DB에 함께 저장
            com.pbl.tasktoolintegration.jira.entity.JiraUser assigneeUser = null;
            if (jiraIssue.getFields().getAssignee() != null) {
                assigneeUser = findUserEntityByJiraId(jiraIssue.getFields().getAssignee().getAccountId())
                        .orElse(null);
            }

            // 생성 및 저장
            com.pbl.tasktoolintegration.jira.entity.JiraIssue newIssue = com.pbl.tasktoolintegration.jira.entity.JiraIssue.builder()
                    .jiraId(jiraIssue.getId())
                    .key(jiraIssue.getKey())
                    .taskCreatedAt(jiraIssue.getFields().getCreated())
                    .assigneeJiraUser(assigneeUser)
                    .build();

            jiraIssueRepository.save(newIssue);
        }
    }

    public List<JiraUser> getJiraUserList() {
        return jiraWebClient
                .get()
                .uri(uriBuilder -> uriBuilder.pathSegment("users", "search")
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<JiraUser>> () {})
                .block();
    }

    public List<JiraIssue> getIssueList() {
        JiraIssueList jiraIssueList = jiraWebClient
                .get()
                .uri(uriBuilder -> uriBuilder.pathSegment("search")
                        .queryParam("jql", "ORDER BY CREATED DESC")
                        .queryParam("fields", "description,comment,assignee,subtasks")
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<JiraIssueList>() {
                })
                .block();

        return Objects.requireNonNull(jiraIssueList).getIssues();
    }

    private Optional<com.pbl.tasktoolintegration.jira.entity.JiraUser> findUserEntityByJiraId(String jiraId) {
        return jiraUserRepository.findByJiraId(jiraId);
    }

    private Optional<com.pbl.tasktoolintegration.jira.entity.JiraIssue> findIssueEntityByJiraId(Long jiraId) {
        return jiraIssueRepository.findByJiraId(jiraId);
    }
}
