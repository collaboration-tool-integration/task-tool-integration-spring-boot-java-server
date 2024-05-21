package com.pbl.tasktoolintegration.jira.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseTimeUnit {
    ALL, MONTHLY, WEEKLY, DAILY
}
