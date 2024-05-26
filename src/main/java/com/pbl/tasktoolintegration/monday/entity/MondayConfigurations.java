package com.pbl.tasktoolintegration.monday.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "monday_configurations")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MondayConfigurations {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "apy_key")
    private String apiKey;

    @Builder
    public MondayConfigurations(String apiKey) {
        this.apiKey = apiKey;
    }
}
