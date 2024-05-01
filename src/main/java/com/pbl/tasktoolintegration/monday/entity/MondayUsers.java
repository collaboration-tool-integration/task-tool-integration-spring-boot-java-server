package com.pbl.tasktoolintegration.monday.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "monday_users")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MondayUsers {
    @Id
    private String id;

    @Column(name = "created_at")
    private Date createdAt;

    @Column
    private String email;

    @Column
    private String name;

    @Column(name = "phone_number")
    private  String phoneNumber;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String url;

    @Builder
    public MondayUsers(String id, Date createdAt, String email, String name, String phoneNumber, String title, String url) {
        this.id = id;
        this.createdAt = createdAt;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.title = title;
        this.url = url;
    }
}
