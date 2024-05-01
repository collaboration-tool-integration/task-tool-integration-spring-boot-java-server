package com.pbl.tasktoolintegration.monday.model;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetAllMondayUsersDto {
    private String id;
    private Date createdAt;
    private String email;
    private String name;
    private String phoneNumber;
    private String title;
    private String url;

    public static GetAllMondayUsersDto from(MondayGetAllUsersRes.User user) {
        return GetAllMondayUsersDto.builder()
                .id(user.getId())
                .createdAt(user.getCreated_at())
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhone())
                .title(user.getTitle())
                .url(user.getUrl())
                .build();
    }
}
