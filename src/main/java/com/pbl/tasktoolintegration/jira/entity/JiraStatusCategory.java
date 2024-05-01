package com.pbl.tasktoolintegration.jira.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.ArrayList;

@Data
@Entity
@Table(name = "JiraStatusCategory", indexes = @Index(columnList = "jiraId", unique = true))
public class JiraStatusCategory {
    // DB 고유 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 지라 조직 내 ID
    @Column(nullable = false)
    private Long jiraId;
    // 상태 카테고리 키
    @Column(nullable = false)
    private String key;
    // 상태 카테고리명
    @Column(nullable = false)
    private String name;
    // 상태 카테고리 색상
    private String colorName;
    @OneToMany(mappedBy = "jiraStatusCategory")
    private List<JiraStatus> jiraStatusList = new ArrayList<>();
}
