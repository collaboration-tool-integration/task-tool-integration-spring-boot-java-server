package com.pbl.tasktoolintegration.monday.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "monday_users")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MondayUsers {
    @Id
    private String id;

    @Builder
    public MondayUsers(String id) {
        this.id = id;
    }
}
