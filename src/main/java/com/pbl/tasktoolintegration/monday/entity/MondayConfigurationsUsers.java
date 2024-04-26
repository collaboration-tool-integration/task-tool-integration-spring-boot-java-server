package com.pbl.tasktoolintegration.monday.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "monday_configurations_users")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@IdClass(MondayConfigurationsUsersId.class)
public class MondayConfigurationsUsers {
    @Id
    @ManyToOne
    @JoinColumn(name = "monday_configurations_id")
    private MondayConfigurations mondayConfiguration;

    @Id
    @ManyToOne
    @JoinColumn(name = "monday_users_id")
    private MondayUsers mondayUser;

    @Builder
    public MondayConfigurationsUsers(MondayConfigurations mondayConfiguration, MondayUsers mondayUser) {
        this.mondayConfiguration = mondayConfiguration;
        this.mondayUser = mondayUser;
    }
}
