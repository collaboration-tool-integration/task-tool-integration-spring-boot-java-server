package com.pbl.tasktoolintegration.jira.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pbl.tasktoolintegration.jira.entity.JiraUser;
import lombok.Getter;

@Getter
public class GetJiraUserDto {

	@JsonProperty("accountId")
	private String accountId;

	@JsonProperty("avatarUrls")
	private AvatarUrls avatarUrls;

	@JsonProperty("displayName")
	private String displayName;

	@JsonProperty("accountType")
	private String accountType;

	@JsonProperty("self")
	private String self;

	@JsonProperty("active")
	private boolean active;

	public JiraUser to() {
		return JiraUser.builder()
				.jiraAccountId(accountId)
				.avatarUrls(avatarUrls != null ? avatarUrls.getJsonMember48x48() : null)
				.jiraAccountType(accountType)
				.displayName(displayName)
				.active(active)
				.build();
	}
}