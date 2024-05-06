package com.pbl.tasktoolintegration.jira.model.dto;

import com.atlassian.adf.jackson2.AdfJackson2;
import com.atlassian.adf.model.node.Doc;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pbl.tasktoolintegration.jira.entity.JiraComment;
import com.pbl.tasktoolintegration.jira.entity.JiraIssue;
import com.pbl.tasktoolintegration.jira.entity.JiraUser;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetJiraIssueCommentDto {
	@JsonProperty("comments")
	private List<CommentsItem> comments;

	@Getter
	public static class CommentsItem {
		@JsonProperty("author")
		private GetJiraUserDto author;

		@JsonProperty("created")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
		private LocalDateTime created;

		@JsonProperty("updateAuthor")
		private GetJiraUserDto updateAuthor;

		@JsonProperty("jsdPublic")
		private boolean jsdPublic;

		@JsonProperty("id")
		private String id;

		@JsonProperty("body")
		private Doc body;

		@JsonProperty("updated")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
		private LocalDateTime updated;

		public JiraComment to(JiraIssue jiraIssue, JiraUser jiraAuthorUser, JiraUser jiraUpdateAuthor) {
			AdfJackson2 adfJackson2 = new AdfJackson2();

			return JiraComment.builder()
					.jiraId(Long.valueOf(id))
					.authorUser(jiraAuthorUser)
					.updateAuthorUser(jiraUpdateAuthor)
					.jiraIssue(jiraIssue)
					.created(created)
					.updated(updated)
					.body(body != null ? adfJackson2.marshall(body) : null)
					.build();
		}
	}
}