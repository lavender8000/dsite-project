package com.lav.dsite.dto;

import com.lav.dsite.entity.OauthAccount;
import com.lav.dsite.enums.OauthProvider;

import lombok.Data;

@Data
public class OauthAccountResponseDto {

    private OauthProvider oauthProvider;

    private String oauthEmail;

    public static OauthAccountResponseDto fromOauthAccount(OauthAccount oauthAccount) {
        OauthAccountResponseDto oauthAccountResponseDto = new OauthAccountResponseDto();
        oauthAccountResponseDto.setOauthProvider(oauthAccount.getOauthProvider());
        oauthAccountResponseDto.setOauthEmail(oauthAccount.getOauthEmail());
        return oauthAccountResponseDto;
    }
}
