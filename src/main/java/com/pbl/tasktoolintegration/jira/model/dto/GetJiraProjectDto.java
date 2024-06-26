package com.pbl.tasktoolintegration.jira.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pbl.tasktoolintegration.jira.entity.JiraProject;
import com.pbl.tasktoolintegration.jira.entity.JiraProjectKey;
import com.pbl.tasktoolintegration.jira.entity.JiraUser;
import lombok.Getter;

import java.util.List;

@Getter
public class GetJiraProjectDto {
	@JsonProperty("avatarUrls")
	private AvatarUrls avatarUrls;

	@JsonProperty("description")
	private String description;

	@JsonProperty("entityId")
	private String entityId;

	@JsonProperty("isPrivate")
	private boolean isPrivate;

	@JsonProperty("uuid")
	private String uuid;

	@JsonProperty("lead")
	private GetJiraUserDto lead;

	@JsonProperty("issueTypes")
	private List<GetJiraIssueTypeDto> issueTypes;

	@JsonProperty("expand")
	private String expand;

	@JsonProperty("simplified")
	private boolean simplified;

	@JsonProperty("name")
	private String name;

	@JsonProperty("self")
	private String self;

	@JsonProperty("style")
	private String style;

	@JsonProperty("id")
	private String id;

	@JsonProperty("projectKeys")
	private List<String> projectKeys;

	@JsonProperty("projectTypeKey")
	private String projectTypeKey;

	@JsonProperty("key")
	private String key;

	public JiraProject to(JiraUser jiraUser) {
		JiraProject project = JiraProject.builder()
				.jiraUuid(uuid)
				.jiraId(Long.parseLong(id))
				.name(name)
				.description(description)
				.typeKey(projectTypeKey)
				.iconUrl(avatarUrls != null ? avatarUrls.getJsonMember48x48() : null)
				.isPrivate(isPrivate)
				.simplified(simplified)
				.leadUser(jiraUser)
				.build();

		List<JiraProjectKey> projectKeyList = projectKeys.stream()
				.map(projectKey -> JiraProjectKey.builder()
						.key(projectKey)
						.jiraProject(project)
						.build())
				.toList();

		project.setJiraProjectKeyList(projectKeyList);

		return project;
	}
}