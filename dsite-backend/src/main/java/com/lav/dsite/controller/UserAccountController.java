package com.lav.dsite.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lav.dsite.common.Result;
import com.lav.dsite.dto.UpdateUserInfoDto;
import com.lav.dsite.dto.UpdateUserPasswordDto;
import com.lav.dsite.dto.UserAccountProfileDto;
import com.lav.dsite.dto.UserRedisDto;
import com.lav.dsite.service.UserService;
import com.lav.dsite.utils.ResultHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class UserAccountController {

    @Autowired
    private UserService userService;

    // 獲取用戶帳戶設定資訊 isPasswordSet、oauthProvider、oauthEmail
    @GetMapping("/users/me/profile")
    @ResponseBody
    public ResponseEntity<Result<UserAccountProfileDto>> getUserAccountProfile(Authentication authentication) {

        UserRedisDto userRedisDto = (UserRedisDto) authentication.getPrincipal();

        UserAccountProfileDto userAccountProfileDto = userService.getUserAccountProfile(userRedisDto.getId());
        
        Result<UserAccountProfileDto> result = Result.success(userAccountProfileDto);
        
        return ResultHandler.getResponseEntity(result);
    }

    // 修改用戶一般資料
    @PutMapping("/users/me")
    @ResponseBody
    public ResponseEntity<Result<Void>> updateUserInfo
    (
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication,
        @RequestBody @Valid UpdateUserInfoDto updateUserInfoDto
    ) {
        
        UserRedisDto userRedisDto = (UserRedisDto) authentication.getPrincipal();

        userService.updateUserInfo(request, response, userRedisDto.getId(), updateUserInfoDto);

        Result<Void> result = Result.success();
        return ResultHandler.getResponseEntity(result);
    }

    // 修改用戶密碼 API
    @PutMapping("/users/me/password")
    @ResponseBody
    public ResponseEntity<Result<Void>> updateUserPassword(Authentication authentication, @RequestBody @Valid UpdateUserPasswordDto updateUserPasswordDto) {

        UserRedisDto userRedisDto = (UserRedisDto) authentication.getPrincipal();

        userService.updateUserPassword(userRedisDto.getId(), updateUserPasswordDto);

        Result<Void> result = Result.success();
        return ResultHandler.getResponseEntity(result);
    }

    // 綁定第三方認證 Google
    @GetMapping("/users/me/link-google")
    public void linkWithGoogle(Authentication authentication, HttpServletResponse response) throws IOException {

        UserRedisDto userRedisDto = (UserRedisDto) authentication.getPrincipal();

        Long id = userRedisDto.getId();

        String redirectUrl = "/oauth2/authorization/google?actionType=link&userId=" + id;

        response.sendRedirect(redirectUrl);

    }
}
