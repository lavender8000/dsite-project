package com.lav.dsite.service;

import com.lav.dsite.dto.UserOauthRegisterDto;
import com.lav.dsite.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public interface OauthAuthService {

    User loginOrRegisterWithOauthAccount(HttpServletResponse response, UserOauthRegisterDto userOauthRegisterDto);

    User linkUserWithOauthAccount(Long userId, UserOauthRegisterDto userOauthRegisterDto);
    
}
