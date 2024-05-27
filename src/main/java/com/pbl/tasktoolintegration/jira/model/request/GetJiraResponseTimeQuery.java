package com.pbl.tasktoolintegration.jira.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetJiraResponseTimeQuery {
    @Nullable
    private Long projectId;
    private ResponseTimeUnit responseTimeUnit;
    private LocalDate targetDate;
}
