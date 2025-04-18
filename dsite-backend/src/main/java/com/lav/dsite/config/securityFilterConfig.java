package com.lav.dsite.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.lav.dsite.filter.JwtAuthenticationTokenFilter;
import com.lav.dsite.security.DsiteOAuth2AuthorizationRequestResolver;

@Configuration
@EnableWebSecurity
public class securityFilterConfig {

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired
    private CorsConfigurationSource corsConfigurationSource;
    @Autowired
    private DsiteOAuth2AuthorizationRequestResolver authorizationRequestResolver;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                .requestMatchers(
                    "/auth/**",
                    "/oauth2/**"
                ).permitAll()
                .requestMatchers(HttpMethod.GET, "/posts/{postId}", "/posts", "/forums", "/users/{userId}/public").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2Login -> oauth2Login
                .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
                    .authorizationRequestResolver(authorizationRequestResolver)
                )
                .successHandler(authenticationSuccessHandler) // 登入成功會重定向到 前端 /login-oauth2-result?status=success
                .failureHandler(authenticationFailureHandler) // 登入失敗會重定向到 前端 /login-oauth2-result?status=failure
            )
            .cors(cors -> cors
                .configurationSource(corsConfigurationSource)
            )
            .csrf(csrf -> csrf
                .disable()
            )
            .sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(authenticationEntryPoint)
            );
            
        return http.build();
    }

}
