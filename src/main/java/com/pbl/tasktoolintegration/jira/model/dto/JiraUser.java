package com.pbl.tasktoolintegration.jira.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pbl.tasktoolintegration.jira.JiraConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class JiraUser {
    @JsonProperty("accountId")
    private String accountId;

    @JsonProperty("accountType")
    private JiraConstants.JiraAccountType accountType;

    @JsonProperty("active")
    private boolean active;

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("emailAddress")
    private String emailAddress;
}
