package com.pbl.tasktoolintegration.jira.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class JiraSearchApiPaging {
	@JsonProperty("total")
	private int total;

	@JsonProperty("maxResults")
	private int maxResults;

	@JsonProperty("startAt")
	private int startAt;

	@JsonProperty("issues")
	private List<GetJiraIssueDto> issues;
}