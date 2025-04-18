package com.lav.dsite.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.lav.dsite.utils.LogManager;

import jakarta.annotation.PostConstruct;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private String issuer;

    private String secret;

    private long expirationInMs;

    private String cookieName;

    private List<ExcludeUrl> excludeUrls;

    @PostConstruct
    public void init() {
        LogManager.info("jwtConfig - Filter excludeUrls", excludeUrls.toString());
    }

    @Data
    public static class ExcludeUrl {
        private String path;
        private List<String> methods;
    }
    
}
