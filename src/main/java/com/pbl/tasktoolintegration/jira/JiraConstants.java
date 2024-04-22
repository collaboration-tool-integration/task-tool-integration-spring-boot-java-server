package com.pbl.tasktoolintegration.jira;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JiraConstants {
    @AllArgsConstructor
    public enum JiraAccountType {
        atlassian("atlassian"),
        app("app"),
        customer("customer");

        private String type;
    }
}
