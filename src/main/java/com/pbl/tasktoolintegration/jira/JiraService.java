package com.pbl.tasktoolintegration.jira;

import com.atlassian.adf.model.Element;
import com.atlassian.adf.model.node.Mention;
import com.atlassian.adf.model.node.Node;
import com.atlassian.adf.model.node.Text;
import com.atlassian.adf.model.node.type.DocContent;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pbl.tasktoolintegration.common.Constants;
import com.pbl.tasktoolintegration.jira.entity.JiraIssueResponse;
import com.pbl.tasktoolintegration.jira.model.dto.*;
import com.pbl.tasktoolintegration.jira.repository.JiraIssueRepository;
import com.pbl.tasktoolintegration.jira.repository.JiraIssueResponseRepository;
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
import java.time.Duration;
import java.time.temporal.ChronoUnit;
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
    private final JiraIssueResponseRepository jiraIssueResponseRepository;

    @Transactional(readOnly = true)
    public List<JiraUserDto.GetJiraUserStatusDto> getAllUserStatuses() {
        return jiraUserRepository.findAllByStatus(Constants.Status.ACTIVE)
                .stream()
                .map(JiraUserDto.GetJiraUserStatusDto::from)
                .toList();
    }

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
            if (!jiraUser.getAccountType().equals(JiraConstants.JiraAccountType.atlassian)) continue;

            log.info(jiraUser.getAccountId());
            // DB에서 해당 ID를 가진 user 찾기
            com.pbl.tasktoolintegration.jira.entity.JiraUser dbJiraUser = findUserEntityByJiraId(jiraUser.getAccountId())
                    .orElse(null);

            // 이미 있다면 패스
            if (dbJiraUser != null) continue;

            // 없다면 생성 후 저장
            jiraUserRepository.saveAndFlush(com.pbl.tasktoolintegration.jira.entity.JiraUser.builder()
                    .name(jiraUser.getDisplayName())
                    .jiraId(jiraUser.getAccountId())
                    .email(jiraUser.getEmailAddress())
                    .build());
        }
    }

    /**
     * Jira 이슈를 DB와 동기화하기
     * TODO: 하위 이슈 처리
     * TODO: 이미 등록된 이슈일 경우에 대한 처리
     */
    @Transactional
    public void syncJiraIssue() {
        // Jira 이슈 목록 API로 조회
        List<JiraIssue> jiraIssueList = getIssueList(new ArrayList<>(), 0, 50);

        log.info("length: " + jiraIssueList.size());

        // 넘어온 목록 loop
        for (JiraIssue jiraIssue : jiraIssueList) {
            // DB에서 해당 ID를 가진 이슈 찾기
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

            // 생성
            com.pbl.tasktoolintegration.jira.entity.JiraIssue newIssue = com.pbl.tasktoolintegration.jira.entity.JiraIssue.builder()
                    .jiraId(jiraIssue.getId())
                    .key(jiraIssue.getKey())
                    .taskCreatedAt(jiraIssue.getFields().getCreated())
                    .assigneeJiraUser(assigneeUser)
                    .build();

            newIssue = jiraIssueRepository.saveAndFlush(newIssue);

            // 연관 유저 목록 조회
            List<com.pbl.tasktoolintegration.jira.entity.JiraUser> relatedUserList = getIssueRelatedUserList(jiraIssue.getFields());
            if (relatedUserList == null) continue;

            // 유저 Loop
            for (com.pbl.tasktoolintegration.jira.entity.JiraUser user : relatedUserList) {
                // 생성일자가 가장 빠른 유저 댓글 조회
                JiraComment comment = jiraIssue.getFields().getComment().getComments()
                        .stream()
                        .filter(jiraComment -> jiraComment.getAuthor().getAccountId().equals(user.getJiraId()))
                        .min(Comparator.comparing(JiraComment::getCreated))
                        .orElse(null);

                // 없으면 패스
                if (comment == null || comment.getCreated() == null) continue;

                // 있다면 응답 목록에 추가
                JiraIssueResponse response = JiraIssueResponse.builder()
                        .jiraUser(user)
                        .jiraIssue(newIssue)
                        .responseSecond(Duration.between(jiraIssue.getFields().getCreated(), comment.getCreated()).toSeconds())
                        .build();

                jiraIssueResponseRepository.saveAndFlush(response);
            }
        }
    }

    private List<com.pbl.tasktoolintegration.jira.entity.JiraUser> getIssueRelatedUserList(JiraIssue.JiraIssueFields fields) {
        if (fields == null || fields.getDescription() == null) return null;
        List<String> relatedUserIdList = new ArrayList<>();

        // 할당자 있을 경우 할당자 포함
        if (fields.getAssignee() != null) {
            relatedUserIdList.add(fields.getAssignee().getAccountId());
        }

        // 멘션된 Jira User ID 목록
        relatedUserIdList.addAll(fields.getDescription().allNodesOfType(Mention.class)
                .map(Mention::id)
                .toList());

        // 중복 삭제
        relatedUserIdList = relatedUserIdList.stream().distinct().toList();

        List<com.pbl.tasktoolintegration.jira.entity.JiraUser> userList = new ArrayList<>();
        // 연관 사용자 ID 목록 Loop
        for (String userId : relatedUserIdList) {
            com.pbl.tasktoolintegration.jira.entity.JiraUser user = jiraUserRepository.findByJiraId(userId)
                    .orElse(null);
            if (user == null) continue;

            // 유저 있는 경우
            userList.add(user);
        }

        return userList;
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

    public List<JiraIssue> getIssueList(List<JiraIssue> jiraIssueList, int startAt, int maxResults) {
        JiraIssueList result = jiraWebClient
                .get()
                .uri(uriBuilder -> uriBuilder.pathSegment("search")
                        .queryParam("jql", "ORDER BY CREATED DESC")
                        .queryParam("startAt", startAt)
                        .queryParam("maxResults", maxResults)
                        .queryParam("fields", "description,comment,assignee,subtasks,created")
                        .build())
                .retrieve()
                .bodyToMono(JiraIssueList.class)
                .block();

        if (result != null && !result.getIssues().isEmpty()) {
            jiraIssueList.addAll(result.getIssues());
            jiraIssueList = getIssueList(jiraIssueList, result.getStartAt() + maxResults, maxResults);
        }
        return jiraIssueList;
    }

    private Optional<com.pbl.tasktoolintegration.jira.entity.JiraUser> findUserEntityByJiraId(String jiraId) {
        return jiraUserRepository.findByJiraId(jiraId);
    }

    private Optional<com.pbl.tasktoolintegration.jira.entity.JiraIssue> findIssueEntityByJiraId(Long jiraId) {
        return jiraIssueRepository.findByJiraId(jiraId);
    }
}
