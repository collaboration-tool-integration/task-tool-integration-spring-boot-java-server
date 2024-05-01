package com.pbl.tasktoolintegration.jira.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IssueTypesItem{

	@JsonProperty("avatarId")
	private int avatarId;

	@JsonProperty("hierarchyLevel")
	private int hierarchyLevel;

	@JsonProperty("name")
	private String name;

	@JsonProperty("self")
	private String self;

	@JsonProperty("description")
	private String description;

	@JsonProperty("id")
	private String id;

	@JsonProperty("iconUrl")
	private String iconUrl;

	@JsonProperty("subtask")
	private boolean subtask;
}