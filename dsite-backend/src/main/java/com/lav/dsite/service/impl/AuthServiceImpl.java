package com.lav.dsite.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lav.dsite.common.Result;
import com.lav.dsite.dto.UserPasswordLoginDto;
import com.lav.dsite.dto.UserPasswordRegisterDto;
import com.lav.dsite.dto.UserRedisDto;
import com.lav.dsite.entity.User;
import com.lav.dsite.enums.DsiteAuthType;
import com.lav.dsite.enums.JwtClaimKey;
import com.lav.dsite.enums.ResponseStatus;
import com.lav.dsite.exception.AuthServiceException;
import com.lav.dsite.security.DsiteUserDetails;
import com.lav.dsite.service.AuthService;
import com.lav.dsite.service.JwtTokenService;
import com.lav.dsite.service.RedisService;
import com.lav.dsite.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.lettuce.core.RedisException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisService redisService;
    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private UserService userService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Map<String, Object>> registerWithPassword(HttpServletResponse response, UserPasswordRegisterDto user) {

        User registeredUser = userService.registerUserWithPassword(user);

        try {
            redisService.saveUser(UserRedisDto.fromUser(registeredUser));

            // TODO: 權限信息 - 寫入，將權限信息寫入redis

        } catch (Exception e) {
            ResponseStatus responseStatus = ResponseStatus.REDIS_ERROR;
            throw new AuthServiceException(responseStatus, e);
        }

        // 生成 JWT token 並寫入 cookie
        String jwtToken = jwtTokenService.generateAccessToken(registeredUser, DsiteAuthType.PASSWORD);
        jwtTokenService.addJwtToCookie(jwtToken, response);

        // 封裝結果
        Map<String, Object> result = new HashMap<>();
        result.put("id", registeredUser.getId());
        result.put("nickname", registeredUser.getNickName());
        result.put("email", registeredUser.getEmail());

        return Result.created(result);
    }

    @Override
    public Result<Map<String, Object>> loginWithPassword(HttpServletResponse response, UserPasswordLoginDto user) {

        Authentication authentication;
        // 驗證用戶
        try {
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
                
            );
        } catch (AuthenticationException e) {
            ResponseStatus responseStatus = ResponseStatus.LOGIN_FAILED;
            throw new AuthServiceException(responseStatus, e);
        }

        DsiteUserDetails dsiteUserDetails = (DsiteUserDetails) authentication.getPrincipal();
        User loginUser = dsiteUserDetails.getUser();
        
        try {
            redisService.saveUser(UserRedisDto.fromUser(loginUser));

            // TODO: 權限信息 - 寫入，將權限信息寫入redis

        } catch (Exception e) {
            ResponseStatus responseStatus = ResponseStatus.REDIS_ERROR;
            throw new AuthServiceException(responseStatus, e);
        }

        // 生成 JWT token 並寫入 cookie
        String jwtToken = jwtTokenService.generateAccessToken(loginUser, DsiteAuthType.PASSWORD);
        jwtTokenService.addJwtToCookie(jwtToken, response);

        // 封裝結果
        Map<String, Object> result = new HashMap<>();
        result.put("id", loginUser.getId());
        result.put("nickname", loginUser.getNickName());
        result.put("email", loginUser.getEmail());

        return Result.success(result);
    }

    @Override
    public Result<Map<String, Object>> logout(HttpServletRequest request, HttpServletResponse response) {

        final String jwt = jwtTokenService.getJwtFromCookie(request);

        // 清除 cookie 中的 JWT
        jwtTokenService.clearJwtFromCookie(response);

        if (jwt == null) {
            return Result.success();
        }

        Date expiration;
        try {
            Claims claims = jwtTokenService.parseToken(jwt);
            expiration = claims.getExpiration();
            if (expiration == null) {
                return Result.success();
            }
        } catch (JwtException e) {
            return Result.success();
        }
        
        Date now = new Date();
        int redisExpiration = (int) ((expiration.getTime() - now.getTime()) / 1000);

        // 將 JWT 加入黑名單
        try {
            redisService.saveJwtBlackList(jwt, redisExpiration);
        } catch (RedisException e) {
            ResponseStatus responseStatus = ResponseStatus.REDIS_ERROR;
            throw new AuthServiceException(responseStatus, e);
        }

        // 清除身份 (可能有通過 oauth 登入的用戶)
        SecurityContextHolder.clearContext();

        return Result.success();
    }

    @Override
    public Result<Map<String, Object>> checkLogin(HttpServletRequest request) {

        final String jwt = jwtTokenService.getJwtFromCookie(request);

        if (jwt == null) {
            return Result.error(ResponseStatus.INVALID_JWT_TOKEN);
        }

        // 驗證 JWT 並獲取 用戶id(subject)
        Date expiration;
        String subject;
        String nickname;
        String email;
        try {
            Claims claims = jwtTokenService.parseToken(jwt);
            subject = claims.getSubject();
            expiration = claims.getExpiration();
            nickname = claims.get(JwtClaimKey.NICKNAME.getKey(), String.class);
            email = claims.get(JwtClaimKey.EMAIL.getKey(), String.class);
            if (expiration == null) {
                return Result.error(ResponseStatus.INVALID_JWT_TOKEN);
            }
        } catch (JwtException e) {
            return Result.error(ResponseStatus.INVALID_JWT_TOKEN);
        }

        // 驗證 JWT 是否在黑名單
        try {
            if (redisService.isJwtBlackList(jwt)) {
                return Result.error(ResponseStatus.INVALID_JWT_TOKEN);
            }
        } catch (RedisException e) {
            return Result.error(ResponseStatus.REDIS_ERROR);
        }
        
        Long id = Long.parseLong(subject);

        // 封裝結果
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("nickname", nickname);
        result.put("email", email);

        return Result.success(result);
    }

}
