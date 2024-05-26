package com.pbl.tasktoolintegration.monday.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterMondayConfigurationRes {
    private Long configurationId;
}
