package com.pbl.tasktoolintegration.jira.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pbl.tasktoolintegration.jira.entity.JiraIssueType;
import com.pbl.tasktoolintegration.jira.entity.JiraProject;
import lombok.Getter;

@Getter
public class GetJiraIssueTypeDto {
	@JsonProperty("untranslatedName")
	private String untranslatedName;

	@JsonProperty("hierarchyLevel")
	private int hierarchyLevel;

	@JsonProperty("name")
	private String name;

	@JsonProperty("description")
	private String description;

	@JsonProperty("id")
	private String id;

	@JsonProperty("iconUrl")
	private String iconUrl;

	@JsonProperty("subtask")
	private boolean subtask;

	public JiraIssueType to(JiraProject jiraProject) {
		return JiraIssueType.builder()
				.jiraId(Long.parseLong(id))
				.name(name)
				.description(description)
				.iconUrl(iconUrl)
				.subtask(subtask)
				.jiraProject(jiraProject)
				.build();
	}
}