package com.pbl.tasktoolintegration.jira.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pbl.tasktoolintegration.jira.config.converter.ResponseTimeUnitConverter;
import com.pbl.tasktoolintegration.jira.model.dto.GetJiraUserDto;
import com.pbl.tasktoolintegration.jira.model.dto.JiraIssueChangeLogDto;
import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

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
