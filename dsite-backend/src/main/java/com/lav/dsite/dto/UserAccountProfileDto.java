package com.lav.dsite.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserAccountProfileDto {

    private boolean isPasswordSet;
    
    private List<OauthAccountResponseDto> oauthAccounts;

}
