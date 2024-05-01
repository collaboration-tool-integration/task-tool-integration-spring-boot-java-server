package com.pbl.tasktoolintegration.jira.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class JiraApiPaging<T> {
	@JsonProperty("total")
	private int total;

	@JsonProperty("isLast")
	private boolean isLast;

	@JsonProperty("maxResults")
	private int maxResults;

	@JsonProperty("self")
	private String self;

	@JsonProperty("startAt")
	private int startAt;

	@JsonProperty("values")
	private List<T> values;
}