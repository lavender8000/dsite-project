package com.lav.dsite.dto;

import com.lav.dsite.entity.User;
import com.lav.dsite.enums.Gender;

import lombok.Data;

@Data
public class UserResponseDto {

    private Long id;

    private String email;

    private String uniqueName;

    private String nickName;

    private Gender gender;

    public static UserResponseDto fromUser(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setUniqueName(user.getUniqueName());
        userResponseDto.setNickName(user.getNickName());
        userResponseDto.setGender(user.getGender());
        return userResponseDto;
    }
}
