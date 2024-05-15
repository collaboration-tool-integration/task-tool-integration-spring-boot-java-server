package com.pbl.tasktoolintegration.jira.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class JiraOrganization {
    // DB 고유 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, nullable = false)

    private Long id;
    // Jira 조직 URL
    @Column(nullable = false)
    private String url;
//
//    @OneToMany(mappedBy = "jiraOrganization")
//    private List<JiraProject> jiraProjectList = new ArrayList<>();
}