package com.pbl.tasktoolintegration.jira.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JiraIssueChangeLogDto {
	@JsonProperty("field")
	private String field;

	@JsonProperty("toString")
	private String toString;

	@JsonProperty("from")
	private String from;

	@JsonProperty("to")
	private String to;

	@JsonProperty("fromString")
	private String fromString;

	@JsonProperty("fieldtype")
	private String fieldtype;
}