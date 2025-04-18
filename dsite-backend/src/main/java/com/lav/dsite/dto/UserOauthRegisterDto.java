package com.lav.dsite.dto;

import com.lav.dsite.enums.OauthProvider;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserOauthRegisterDto {

    @Email
    @Size(max = 320)
    @NotBlank
    private String oauthEmail;

    @Size(max = 255)
    @NotBlank
    private String oauthId;

    @NotNull
    private OauthProvider oauthProvider;

    @Size(max = 50)
    @NotBlank
    private String nickName;
    
}
