package com.lav.dsite.service;

import java.util.Map;

import com.lav.dsite.common.Result;
import com.lav.dsite.dto.UserPasswordLoginDto;
import com.lav.dsite.dto.UserPasswordRegisterDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    Result<Map<String, Object>> loginWithPassword(HttpServletResponse response, UserPasswordLoginDto user);

    Result<Map<String, Object>> registerWithPassword(HttpServletResponse response, UserPasswordRegisterDto user);

    Result<Map<String, Object>> logout(HttpServletRequest request, HttpServletResponse response);

    Result<Map<String, Object>> checkLogin(HttpServletRequest request);

}
