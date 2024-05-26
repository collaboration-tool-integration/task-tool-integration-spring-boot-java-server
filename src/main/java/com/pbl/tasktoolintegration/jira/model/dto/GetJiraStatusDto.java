package com.pbl.tasktoolintegration.jira.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pbl.tasktoolintegration.jira.entity.JiraIssueType;
import com.pbl.tasktoolintegration.jira.entity.JiraProject;
import com.pbl.tasktoolintegration.jira.entity.JiraStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetJiraStatusDto {
	@JsonProperty("id")
	private String issueTypeId;

	@JsonProperty("name")
	private String issueTypeName;

	@JsonProperty("subtask")
	private Boolean isSubtaskIssueType;

	@JsonProperty("statuses")
	private List<JiraStatusDto> statuses;

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class JiraStatusDto {
		@JsonProperty("untranslatedName")
		private String untranslatedName;

		@JsonProperty("scope")
		private Scope scope;

		@JsonProperty("name")
		private String name;

		@JsonProperty("self")
		private String self;

		@JsonProperty("description")
		private String description;

		@JsonProperty("iconUrl")
		private String iconUrl;

		@JsonProperty("id")
		private String id;

		@JsonProperty("statusCategory")
		private StatusCategory statusCategory;

		@Getter
		@AllArgsConstructor
		@NoArgsConstructor
		private static class Project {
			@JsonProperty("id")
			private String id;
		}

		@Getter
		@AllArgsConstructor
		@NoArgsConstructor
		public class Scope {
			@JsonProperty("project")
			private Project project;

			@JsonProperty("type")
			private String type;
		}

		@Getter
		@AllArgsConstructor
		@NoArgsConstructor
		public static class StatusCategory {

			@JsonProperty("colorName")
			private String colorName;

			@JsonProperty("name")
			private String name;

			@JsonProperty("self")
			private String self;

			@JsonProperty("id")
			private int id;

			@JsonProperty("key")
			private String key;
		}

		public JiraStatus to(JiraProject jiraProject, JiraIssueType jiraIssueType) {
			return JiraStatus.builder()
					.jiraId(Long.parseLong(id))
					.description(description)
					.name(name)
					.jiraProject(jiraProject)
					.jiraIssueType(jiraIssueType)
					.jiraStatusCategory(statusCategory.key)
					.build();
		}
	}
}