package com.pbl.tasktoolintegration.jira.model.dto;

import com.atlassian.adf.jackson2.AdfJackson2;
import com.atlassian.adf.model.node.Doc;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pbl.tasktoolintegration.jira.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetJiraIssueDto {
	@JsonProperty("id")
	private String id;

	@JsonProperty("fields")
	private Fields fields;

	@JsonProperty("key")
	private String key;

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Fields {
		@JsonProperty("statuscategorychangedate")
		private String statuscategorychangedate;

		@JsonProperty("issuetype")
		private GetJiraIssueTypeDto issuetype;

		@JsonProperty("project")
		private GetJiraProjectDto project;

		@JsonProperty("parent")
		private GetJiraIssueDto parent;

		@JsonProperty("description")
		private Doc description;

		@JsonProperty("summary")
		private String summary;

		@JsonProperty("comment")
		private GetJiraIssueCommentDto comment;

		@JsonProperty("creator")
		private GetJiraUserDto creator;

		@JsonProperty("reporter")
		private GetJiraUserDto reporter;

		@JsonProperty("priority")
		private Priority priority;

		@JsonProperty("assignee")
		private GetJiraUserDto assignee;

		@JsonProperty("created")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
		private LocalDateTime created;

		@JsonProperty("updated")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
		private LocalDateTime updated;

		@JsonProperty("resolutiondate")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
		private LocalDateTime resolutionDate;

		@JsonProperty("duedate")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
		private LocalDate duedate;

		@JsonProperty("status")
		private GetJiraStatusDto.JiraStatusDto status;

		@Getter
		public static class Priority {
			@JsonProperty("name")
			private String name;

			@JsonProperty("self")
			private String self;

			@JsonProperty("iconUrl")
			private String iconUrl;

			@JsonProperty("id")
			private String id;
		}
	}

	public JiraIssue to(
			JiraProject jiraProject,
			JiraIssue parentIssue,
			JiraIssueType jiraIssueType,
			JiraStatus jiraStatus,
			JiraUser assigneeUser,
			JiraUser creatorUser,
			JiraUser reportUser,
			List<JiraUser> jiraUserList) {
		AdfJackson2 adfJackson2 = new AdfJackson2();

		JiraIssue jiraIssue = JiraIssue.builder()
				.jiraId(Long.parseLong(id))
				.key(key)
				.summary(fields.summary)
				.parentIssue(parentIssue)
				.duedate(fields.duedate)
				.description(fields.description != null ? adfJackson2.marshall(fields.description) : null)
				.issuePriority(fields.priority.name)
				.created(fields.created)
				.updated(fields.updated)
				.resolutionDate(fields.resolutionDate)
				.assigneeUser(assigneeUser)
				.creatorUser(creatorUser)
				.reportUser(reportUser)
				.jiraIssueType(jiraIssueType)
				.jiraProject(jiraProject)
				.jiraStatus(jiraStatus)
				.build();

		jiraIssue.setJiraCommentList(fields.comment.getComments().stream()
				.map(commentsItem -> commentsItem.to(
						jiraIssue,
						jiraUserList.stream()
								.filter(jiraUser -> jiraUser.getJiraAccountId().equals(commentsItem.getAuthor().getAccountId()))
								.findFirst()
								.orElse(null),
						jiraUserList.stream()
								.filter(jiraUser -> jiraUser.getJiraAccountId().equals(commentsItem.getUpdateAuthor().getAccountId()))
								.findFirst()
								.orElse(null)
				)).toList());


		return jiraIssue;
	}
}