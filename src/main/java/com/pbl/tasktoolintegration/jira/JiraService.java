package com.pbl.tasktoolintegration.jira;

import com.pbl.tasktoolintegration.jira.entity.JiraProject;
import com.pbl.tasktoolintegration.jira.entity.JiraUser;
import com.pbl.tasktoolintegration.jira.model.dto.GetJiraProjectDto;
import com.pbl.tasktoolintegration.jira.model.dto.GetJiraUserDto;
import com.pbl.tasktoolintegration.jira.model.dto.JiraApiPaging;
import com.pbl.tasktoolintegration.jira.repository.JiraProjectRepository;
import com.pbl.tasktoolintegration.jira.repository.JiraUserRepository;
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
            JiraApiPaging<GetJiraProjectDto> fetchProjectResult = getJiraProjectDtoList(startAt, 100);
            isLast = fetchProjectResult.isLast();
            startAt = fetchProjectResult.getStartAt() + 100;
            if (fetchProjectResult.getValues() == null || fetchProjectResult.getValues().isEmpty())
                break;

            projectList.addAll(fetchProjectResult.getValues().stream()
                    .filter(getJiraProjectDto -> jiraProjectRepository.findByJiraId(Long.getLong(getJiraProjectDto.getId())).isEmpty())
                    .map(getJiraProjectDto -> getJiraProjectDto.to(null))
                    .toList());
        }

        jiraProjectRepository.saveAllAndFlush(projectList);
    }

    private JiraApiPaging<GetJiraProjectDto> getJiraProjectDtoList(int startAt, int maxResult) {
        return jiraWebClient
                .get()
                .uri(uriBuilder -> uriBuilder.pathSegment("project", "search")
                        .queryParam("startAt", startAt)
                        .queryParam("maxResult", maxResult)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<JiraApiPaging<GetJiraProjectDto>>() {})
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
