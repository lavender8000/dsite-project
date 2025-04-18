package com.lav.dsite.service;

import com.lav.dsite.dto.UpdateUserInfoDto;
import com.lav.dsite.dto.UpdateUserPasswordDto;
import com.lav.dsite.dto.UserAccountProfileDto;
import com.lav.dsite.dto.UserOauthRegisterDto;
import com.lav.dsite.dto.UserPasswordRegisterDto;
import com.lav.dsite.entity.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

    User saveUser(User user);

    User findUserById(Long userId);

    User findUserByEmail(String email);

    User registerUserWithPassword(UserPasswordRegisterDto user);

    User registerUserWithOauth(UserOauthRegisterDto userOauthRegisterDto);

    UserAccountProfileDto getUserAccountProfile(Long userId);

    void updateUserPassword(Long userId, UpdateUserPasswordDto updateUserPasswordDto);

    void updateUserInfo(HttpServletRequest request, HttpServletResponse response, Long userId, UpdateUserInfoDto updateUserInfoDto);

}
