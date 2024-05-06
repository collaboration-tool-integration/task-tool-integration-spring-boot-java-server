package com.pbl.tasktoolintegration.jira;

import com.pbl.tasktoolintegration.jira.entity.*;
import com.pbl.tasktoolintegration.jira.model.dto.*;
import com.pbl.tasktoolintegration.jira.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class JiraService {
    private final WebClient jiraWebClient;
    private final JiraProjectRepository jiraProjectRepository;
    private final JiraUserRepository jiraUserRepository;
    private final JiraStatusRepository jiraStatusRepository;
    private final JiraIssueRepository jiraIssueRepository;
    private final JiraIssueTypeRepository jiraIssueTypeRepository;

    @Transactional
    public void syncJiraUser() {
        List<GetJiraUserDto> fetchUserResult = getJiraUserList();
        ArrayList<JiraUser> jiraUserList = new ArrayList<>(fetchUserResult.stream()
                .filter(getJiraUserDto -> getJiraUserDto.getAccountType().equals("atlassian"))
                .filter(getJiraUserDto -> jiraUserRepository.findByJiraAccountId(getJiraUserDto.getAccountId()).isEmpty())
                .map(GetJiraUserDto::to)
                .toList());

        jiraUserRepository.saveAllAndFlush(jiraUserList);
    }

    @Transactional
    public void syncJiraProject() {
        boolean isLast = false;
        ArrayList<JiraProject> projectList = new ArrayList<>();

        int startAt = 0;
        while (!isLast) {
            JiraApiPaging<GetJiraProjectDto> fetchProjectResult = getJiraProjectList(startAt, 100);
            isLast = fetchProjectResult.isLast();
            startAt = fetchProjectResult.getStartAt() + 100;
            if (fetchProjectResult.getValues() == null || fetchProjectResult.getValues().isEmpty())
                break;

            projectList.addAll(fetchProjectResult.getValues().stream()
                    .filter(getJiraProjectDto -> jiraProjectRepository.findByJiraId(Long.getLong(getJiraProjectDto.getId())).isEmpty())
                    .map(getJiraProjectDto -> getJiraProjectDto.to(
                            getJiraProjectDto.getLead() != null && getJiraProjectDto.getLead().getAccountId() != null ?
                                    jiraUserRepository.findByJiraAccountId(getJiraProjectDto.getLead().getAccountId())
                                    .orElse(null) : null
                    ))
                    .toList());
        }

        List<JiraProject> createdProjectList = jiraProjectRepository.saveAllAndFlush(projectList);

        // Project Issue Type 저장
        createdProjectList.forEach(this::saveProjectIssueType);

        // Project Status 저장
        createdProjectList.forEach(this::saveProjectStatus);

        // Issue 저장
        createdProjectList.forEach(this::saveJiraIssues);
    }

    private void saveJiraIssues(JiraProject jiraProject) {
        int startAt = 0;

        while (true) {
            JiraSearchApiPaging fetchIssueResult = getJiraParentIssueList(startAt, 50);
            if (fetchIssueResult.getIssues() == null || fetchIssueResult.getIssues().isEmpty())
                break;

            List<JiraIssue> issueList = fetchIssueResult.getIssues().stream()
                            .filter(getJiraIssueDto -> jiraIssueRepository.findByJiraIdAndJiraProject(Long.getLong(getJiraIssueDto.getId()), jiraProject).isEmpty())
                            .map(getJiraIssueDto -> getJiraIssueDto.to(
                                    jiraProject,
                                    null,
                                    getJiraIssueDto.getFields().getIssuetype() != null ? jiraIssueTypeRepository.findByJiraIdAndJiraProject(Long.valueOf(getJiraIssueDto.getFields().getIssuetype().getId()), jiraProject).orElse(null) : null,
                                    getJiraIssueDto.getFields().getStatus() != null ? jiraStatusRepository.findByJiraIdAndJiraProject(Long.valueOf(getJiraIssueDto.getFields().getStatus().getId()), jiraProject).orElse(null) : null,
                                    jiraUserRepository.findAll())
                            )
                    .toList();

            jiraIssueRepository.saveAll(issueList);

            startAt += fetchIssueResult.getMaxResults();
        }


        saveJiraSubtask(jiraProject);
    }

    private void saveJiraSubtask(JiraProject jiraProject) {
        int startAt = 0;

        while (true) {
            JiraSearchApiPaging fetchIssueResult = getJiraSubtaskList(startAt, 50);
            if (fetchIssueResult.getIssues() == null || fetchIssueResult.getIssues().isEmpty())
                break;

            List<JiraIssue> issueList = fetchIssueResult.getIssues().stream()
                    .filter(getJiraIssueDto -> jiraIssueRepository.findByJiraIdAndJiraProject(Long.getLong(getJiraIssueDto.getId()), jiraProject).isEmpty())
                    .map(getJiraIssueDto -> getJiraIssueDto.to(
                            jiraProject,
                            getJiraIssueDto.getFields().getParent() != null ? findIssue(jiraProject, Long.valueOf(getJiraIssueDto.getFields().getParent().getId())) : null,
                            getJiraIssueDto.getFields().getIssuetype() != null ? jiraIssueTypeRepository.findByJiraIdAndJiraProject(Long.valueOf(getJiraIssueDto.getFields().getIssuetype().getId()), jiraProject).orElse(null) : null,
                            getJiraIssueDto.getFields().getStatus() != null ? jiraStatusRepository.findByJiraIdAndJiraProject(Long.valueOf(getJiraIssueDto.getFields().getStatus().getId()), jiraProject).orElse(null) : null,
                            jiraUserRepository.findAll())
                    )
                    .toList();

            jiraIssueRepository.saveAll(issueList);



            startAt += fetchIssueResult.getMaxResults();
        }

    }

    private JiraIssue findIssue(JiraProject jiraProject, Long targetId) {
        return jiraIssueRepository.findByJiraIdAndJiraProject(targetId, jiraProject)
                .orElse(null);
    }

    private void saveProjectStatus(JiraProject jiraProject) {
        JiraProjectKey jiraProjectKey = jiraProject.getJiraProjectKeyList().stream().findFirst().orElse(null);
        if (jiraProjectKey == null) return;

        getJiraProjectStatuses(jiraProjectKey.getKey())
                .forEach(getJiraStatusDto -> {
                    JiraIssueType issueType = jiraIssueTypeRepository.findByJiraIdAndJiraProject(Long.valueOf(getJiraStatusDto.getIssueTypeId()), jiraProject).orElse(null);
                    ArrayList<JiraStatus> jiraStatuses = new ArrayList<>(getJiraStatusDto.getStatuses().stream()
                            .filter(statusDto -> jiraStatusRepository.findByJiraIdAndJiraProject(Long.valueOf(statusDto.getId()), jiraProject).isEmpty())
                            .map(statusDto -> statusDto.to(jiraProject, issueType))
                            .toList());
                    jiraStatusRepository.saveAllAndFlush(jiraStatuses);
                });
    }

    private void saveProjectIssueType(JiraProject jiraProject) {
        List<JiraIssueType> jiraIssueTypeList = getJiraProjectIssueTypeList(String.valueOf(jiraProject.getJiraId()))
                .stream()
                .filter(getJiraIssueTypeDto -> jiraIssueTypeRepository.findByJiraIdAndJiraProject(Long.valueOf(getJiraIssueTypeDto.getId()), jiraProject).isEmpty())
                .map(getJiraIssueTypeDto -> getJiraIssueTypeDto.to(jiraProject))
                .toList();

        jiraIssueTypeRepository.saveAllAndFlush(jiraIssueTypeList);
    }

    private JiraSearchApiPaging getJiraParentIssueList(int startAt, int maxResult) {
        return jiraWebClient
                .get()
                .uri(uriBuilder -> uriBuilder.pathSegment( "search")
                        .queryParam("jql", "type NOT IN subTaskIssueTypes() ORDER BY CREATED ASC")
                        .queryParam("startAt", startAt)
                        .queryParam("maxResult", maxResult)
                        .queryParam("fields", "*all")
                        .queryParam("expand", "comment")
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<JiraSearchApiPaging>() {})
                .block();
    }

    private JiraSearchApiPaging getJiraSubtaskList(int startAt, int maxResult) {
        return jiraWebClient
                .get()
                .uri(uriBuilder -> uriBuilder.pathSegment( "search")
                        .queryParam("jql", "type IN subTaskIssueTypes() ORDER BY CREATED ASC")
                        .queryParam("startAt", startAt)
                        .queryParam("maxResult", maxResult)
                        .queryParam("fields", "*all")
                        .queryParam("expand", "comment")
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<JiraSearchApiPaging>() {})
                .block();
    }


    private JiraApiPaging<GetJiraProjectDto> getJiraProjectList(int startAt, int maxResult) {
        return jiraWebClient
                .get()
                .uri(uriBuilder -> uriBuilder.pathSegment("project", "search")
                        .queryParam("startAt", startAt)
                        .queryParam("maxResult", maxResult)
                        .queryParam("expand", "description,lead,issueTypes,url,projectKeys")
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<JiraApiPaging<GetJiraProjectDto>>() {})
                .block();
    }

    private List<GetJiraStatusDto> getJiraProjectStatuses(String projectIdOrKey) {
        return jiraWebClient
                .get()
                .uri(uriBuilder -> uriBuilder.pathSegment("project", projectIdOrKey, "statuses")
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GetJiraStatusDto>>() {})
                .block();
    }

    private List<GetJiraIssueTypeDto> getJiraProjectIssueTypeList(String projectId) {
        return jiraWebClient
                .get()
                .uri(uriBuilder -> uriBuilder.pathSegment("issuetype", "project")
                        .queryParam("projectId", projectId)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GetJiraIssueTypeDto>>() {})
                .block();
    }

    private List<GetJiraUserDto> getJiraUserList() {
        return jiraWebClient
                .get()
                .uri(uriBuilder -> uriBuilder.pathSegment("users", "search")
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GetJiraUserDto>>() {})
                .block();
    }

}
