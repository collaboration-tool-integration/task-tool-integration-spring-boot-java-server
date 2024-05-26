package com.pbl.tasktoolintegration.jira.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Table(name = "JiraProjectKey")
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class JiraProjectKey {
    // DB 고유 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, nullable = false)
    private Long id;

    // 프로젝트 Key
    @Column(nullable = false)
    private String key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jiraProjectId")
    private JiraProject jiraProject;
}
