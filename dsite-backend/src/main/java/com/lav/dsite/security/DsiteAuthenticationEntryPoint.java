package com.lav.dsite.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lav.dsite.common.Result;
import com.lav.dsite.enums.ResponseStatus;
import com.lav.dsite.exception.DsiteAuthenticationException;
import com.lav.dsite.utils.LogManager;
import com.lav.dsite.utils.ResponseHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DsiteAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        
        // Dsite 身分驗證失敗，返回錯誤訊息
        if (authException instanceof DsiteAuthenticationException) {

            LogManager.exceptionWithUrl(authException, request);

            DsiteAuthenticationException dsiteAuthenticationException = (DsiteAuthenticationException) authException;
            HttpStatus status = HttpStatus.valueOf(dsiteAuthenticationException.getResponseStatus().getCode());
            Result<Object> body = Result.error(dsiteAuthenticationException.getResponseStatus());

            ResponseHandler.writeResponseAsJson(response, body, status, objectMapper);
            return;

        }

        // 身分未驗證成功，返回錯誤訊息
        if (authException instanceof InsufficientAuthenticationException) {

            LogManager.exceptionWithUrl(authException, request);

            HttpStatus status = HttpStatus.valueOf(ResponseStatus.UNAUTHORIZED.getCode());
            Result<Object> body = Result.error(ResponseStatus.UNAUTHORIZED);

            ResponseHandler.writeResponseAsJson(response, body, status, objectMapper);
            return;

        }

        // 其他未驗證成功，返回錯誤訊息
        LogManager.exceptionWithUrl(authException, request);

        HttpStatus status = HttpStatus.valueOf(ResponseStatus.UNAUTHORIZED.getCode());
        Result<Object> body = Result.error(ResponseStatus.UNAUTHORIZED);

        ResponseHandler.writeResponseAsJson(response, body, status, objectMapper);
        return;

    }

}
