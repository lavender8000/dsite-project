package com.lav.dsite.dto;

import java.time.LocalDateTime;

import com.lav.dsite.entity.User;
import com.lav.dsite.enums.Gender;

import lombok.Data;

@Data
public class UserRedisDto {

    private Long id;

    private String email;

    private String uniqueName;

    private String nickName;

    private Gender gender;

    private String accessToken;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    public static UserRedisDto fromUser(User user) {
        UserRedisDto userRedisDto = new UserRedisDto();
        userRedisDto.setId(user.getId());
        userRedisDto.setEmail(user.getEmail());
        userRedisDto.setUniqueName(user.getUniqueName());
        userRedisDto.setNickName(user.getNickName());
        userRedisDto.setGender(user.getGender());
        userRedisDto.setUpdatedAt(user.getUpdatedAt());
        userRedisDto.setCreatedAt(user.getCreatedAt());
        userRedisDto.setDeletedAt(user.getDeletedAt());
        return userRedisDto;
    }

    public static UserRedisDto fromUser(User user, String accessToken) {
        UserRedisDto userRedisDto = fromUser(user);
        userRedisDto.setAccessToken(accessToken);
        return userRedisDto;
    }

}
