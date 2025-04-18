package com.lav.dsite.service;

import com.lav.dsite.entity.User;
import com.lav.dsite.enums.DsiteAuthType;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface JwtTokenService {

    String generateAccessToken(User user, DsiteAuthType dsiteAuthType);

    Claims parseToken(String token);
    
    void addJwtToCookie(String token, HttpServletResponse response);

    String getJwtFromCookie(HttpServletRequest request);

    void clearJwtFromCookie(HttpServletResponse response);

    String updateAccessToken(String jwt, String nickname);
}
