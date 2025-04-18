package com.lav.dsite.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lav.dsite.config.JwtConfig;
import com.lav.dsite.config.JwtConfig.ExcludeUrl;
import com.lav.dsite.dto.UserRedisDto;
import com.lav.dsite.enums.ResponseStatus;
import com.lav.dsite.exception.DsiteAuthenticationException;
import com.lav.dsite.exception.RedisAuthenticationException;
import com.lav.dsite.service.JwtTokenService;
import com.lav.dsite.service.RedisService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.lettuce.core.RedisException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String jwt = jwtTokenService.getJwtFromCookie(request);

        final String requestURI = request.getRequestURI();

        final String requestMethod = request.getMethod();

        final List<ExcludeUrl> excludeUrls = jwtConfig.getExcludeUrls();
        
        // 不需要驗證的 URL，直接放行
        for (ExcludeUrl excludeUrl : excludeUrls) {
            if (antPathMatcher.match(excludeUrl.getPath(), requestURI) && excludeUrl.getMethods().contains(requestMethod)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        // JWT 不存在，拋出異常 (未登入狀態)
        if (jwt == null) {
            authenticationEntryPoint.commence(request, response, new DsiteAuthenticationException(ResponseStatus.INVALID_JWT_TOKEN));
            return;
        }

        try {
            // JWT 存在，且在黑名單中，拋出異常 (未登入狀態)
            if (redisService.isJwtBlackList(jwt)) {
                authenticationEntryPoint.commence(request, response, new DsiteAuthenticationException(ResponseStatus.INVALID_JWT_TOKEN));
                return;
            }

            // 解析 JWT，若為無效的 JWT，拋出異常 (未登入狀態)
            Claims claims = jwtTokenService.parseToken(jwt);
            String userId = claims.getSubject();
            
            // 獲取 User 信息，從 redis 中獲取
            UserRedisDto userRedisDto = redisService.getUser(userId);
            if (userRedisDto == null) {
                authenticationEntryPoint.commence(request, response, new DsiteAuthenticationException(ResponseStatus.LOGIN_FAILED));
                return;
            }

            // TODO: 權限信息 - 讀取並封裝，封裝權限信息(從redis讀取)，封裝成 authorities 添加到 authentication

            // 封裝 Authentication (授權登入狀態)
            Authentication authentication = new UsernamePasswordAuthenticationToken(userRedisDto, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (ExpiredJwtException e) {
            // TODO: 過期的 JWT，優化
            ResponseStatus responseStatus = ResponseStatus.JWT_EXPIRED;
            authenticationEntryPoint.commence(request, response, new DsiteAuthenticationException(responseStatus, e));
            return;

        } catch (JwtException e) {
            ResponseStatus responseStatus = ResponseStatus.INVALID_JWT_TOKEN;
            authenticationEntryPoint.commence(request, response, new DsiteAuthenticationException(responseStatus, e));
            return;
        } catch (RedisException e) {
            ResponseStatus responseStatus = ResponseStatus.REDIS_ERROR;
            authenticationEntryPoint.commence(request, response, new RedisAuthenticationException(responseStatus, e));
            return;
        }

        filterChain.doFilter(request, response);
    }
    
}
