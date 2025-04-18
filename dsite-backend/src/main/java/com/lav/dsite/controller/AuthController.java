package com.lav.dsite.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lav.dsite.common.Result;
import com.lav.dsite.dto.UserPasswordLoginDto;
import com.lav.dsite.dto.UserPasswordRegisterDto;
import com.lav.dsite.service.AuthService;
import com.lav.dsite.utils.ResultHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/auth/register")
    @ResponseBody
    public ResponseEntity<Result<Map<String,Object>>> register(HttpServletResponse response, @RequestBody @Valid UserPasswordRegisterDto user) {

        Result<Map<String,Object>> result = authService.registerWithPassword(response, user);

        return ResultHandler.getResponseEntity(result);
        
    }
    
    @PostMapping("/auth/login")
    @ResponseBody
    public ResponseEntity<Result<Map<String,Object>>> login(HttpServletResponse response, @RequestBody @Valid UserPasswordLoginDto user) {
        
        Result<Map<String,Object>> result = authService.loginWithPassword(response, user);

        return ResultHandler.getResponseEntity(result);

    }

    @GetMapping("/auth/login-oauth2/google")
    public void loginGoogle(HttpServletResponse response) throws IOException {

        String redirectUrl = "/oauth2/authorization/google?actionType=login";
        
        response.sendRedirect(redirectUrl);

    }

    @PostMapping("/auth/logout")
    @ResponseBody
    public ResponseEntity<Result<Map<String, Object>>> logout(HttpServletRequest request, HttpServletResponse response) {

        Result<Map<String, Object>> result = authService.logout(request, response);

        return ResultHandler.getResponseEntity(result);

    }

    @GetMapping("/auth/check-login")
    @ResponseBody
    public ResponseEntity<Result<Map<String, Object>>> checkLogin(HttpServletRequest request) {

        Result<Map<String, Object>> result = authService.checkLogin(request);

        return ResultHandler.getResponseEntity(result);

    }
}
