package com.pbl.tasktoolintegration.jira.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AvatarUrls {
	
	@JsonProperty("48x48")
	private String jsonMember48x48;

	@JsonProperty("24x24")
	private String jsonMember24x24;

	@JsonProperty("16x16")
	private String jsonMember16x16;

	@JsonProperty("32x32")
	private String jsonMember32x32;
}