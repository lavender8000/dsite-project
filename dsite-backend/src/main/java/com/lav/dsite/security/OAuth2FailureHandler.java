package com.lav.dsite.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.lav.dsite.exception.DsiteAuthenticationException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

    @Value("${frontend.redirectUrl.login}")
    private String redirectLoginUrl;
    
    @Value("${frontend.redirectUrl.link}")
    private String redirectLinkUrl;
    
    @Value("${frontend.redirectUrl.home}")
    private String redirectHomeUrl;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        
        Map<String, String> stateMap = parseState(request.getParameter("state"));
        String redirectUrl;
        switch (stateMap.get("actionType")) {
            case "login":
                redirectUrl = redirectLoginUrl;
                break;
            case "link":
                redirectUrl = redirectLinkUrl;
                break;
            case "unknown":
            default:
                redirectUrl = redirectHomeUrl;
        }

        if (exception instanceof DsiteAuthenticationException) {
            DsiteAuthenticationException dsiteAuthenticationException = (DsiteAuthenticationException) exception;
            String error = dsiteAuthenticationException.getResponseStatus().name();
            response.sendRedirect(redirectUrl + "?status=FAILURE&error=" + error);
            return;
        }

        response.sendRedirect(redirectUrl + "?status=FAILURE");

    }

    // 解析傳來的 state
    private Map<String, String> parseState(String state) {
        Map<String, String> map = new HashMap<>();
        if (state == null) {
            return map;
        }
        String[] split = state.split("&");
        for (String s : split) {
            if (s.startsWith("actionType=")) {
                map.put("actionType", s.split("=")[1]);
            } else if (s.startsWith("userId=")) {
                map.put("userId", s.split("=")[1]);
            }
        }
        return map;
    }

}
